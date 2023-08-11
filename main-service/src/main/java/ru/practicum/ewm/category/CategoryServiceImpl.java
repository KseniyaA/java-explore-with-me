package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.ConflictOperationException;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public Category add(Category newCategory) {
        return categoryRepository.save(newCategory);
    }

    @Transactional
    @Override
    public Category update(Category newCategory) {
        /*List<Category> categoriesByName = categoryRepository.findAll().stream()
                .filter(x -> x.getName().toLowerCase().equals(newCategory.getName().toLowerCase())
                        && !x.getId().equals(newCategory.getId()))
                .collect(Collectors.toList());
        if (!categoriesByName.isEmpty()) {
            throw new ConflictOperationException("Категория с наименованием = " + newCategory.getName() + " уже существует");
        }*/
        return categoryRepository.save(newCategory);
    }

    @Transactional
    @Override
    public void delete(Long id) {
//        boolean existsByCategory = ;
//        List<Event> eventsByCategory = eventRepository.findByCategory(getById(id));
        if (eventRepository.existsByCategory(getById(id))) {
            throw new ConflictOperationException("Удаление невозможно, так как существуют события по данной категории");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getAll(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").ascending());
        Page<Category> all = categoryRepository.findAll(page);
        return all.getContent();
    }

    @Override
    public Category getById(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new EntityNotFoundException("Категория с id = " + catId + " не существует");
        });
    }
}
