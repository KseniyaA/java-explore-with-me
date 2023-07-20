package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category update(Category category) {
        categoryRepository.findById(category.getId()).orElseThrow(() -> {
            throw new EntityNotFoundException("Категория с id = " + category.getId() + " не существует");
        });
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Категория с id = " + id + " не существует");
        });
        categoryRepository.deleteById(id);
    }
}
