package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDtoRequest;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public Compilation add(Compilation compilation) {
        if (compilation.getPinned() == null) {
            compilation.setPinned(Boolean.FALSE);
        }
        List<Event> events = eventRepository.findAllById(compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList()));
        Compilation createdCompilation = compilationRepository.save(compilation);
        createdCompilation.setEvents(events);
        return createdCompilation;
    }

    @Override
    public Compilation getById(long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            throw new EntityNotFoundException("Подборка с id = " + compId + " не существует");
        });
    }

    @Override
    public List<Compilation> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        if (pinned != null) {
            Page<Compilation> compilationByPage = compilationRepository.findAllByPinned(pinned, page);
            return compilationByPage.getContent();
        } else {
            return compilationRepository.findAll(page).getContent();
        }
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        getById(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public Compilation update(Long compId, CompilationDtoRequest dtoRequest) {
        Compilation compilation = getById(compId);
        if (dtoRequest.getEvents() != null && !dtoRequest.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(dtoRequest.getEvents());
            compilation.setEvents(events);
        }
        compilation.setTitle(dtoRequest.getTitle() != null ? dtoRequest.getTitle() : compilation.getTitle());
        compilation.setPinned(dtoRequest.getPinned() != null ? dtoRequest.getPinned() : compilation.getPinned());
        return compilationRepository.save(compilation);
    }

}
