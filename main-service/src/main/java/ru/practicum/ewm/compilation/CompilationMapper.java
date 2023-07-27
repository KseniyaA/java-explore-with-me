package ru.practicum.ewm.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.CompilationDtoRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;

import java.util.Collections;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public Compilation toCompilation(CompilationDtoRequest compilationDto) {
        return Compilation.builder()
                .events(compilationDto.getEvents() == null || compilationDto.getEvents().isEmpty() ? Collections.emptyList()
                        : compilationDto.getEvents().stream().map(x -> Event.builder().id(x).build()).collect(Collectors.toList()))
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .build();
    }

    public CompilationDtoResponse toCompilationDtoResponse(Compilation compilation) {
        return CompilationDtoResponse.builder()
                .id(compilation.getId())
                .events(compilation.getEvents() == null || compilation.getEvents().isEmpty() ? Collections.emptyList()
                        : compilation.getEvents().stream().map(EventMapper::toEventShortDtoResponse).collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
