package ru.practicum.ewm.category;

public interface CategoryService {
    Category add(Category category);

    Category update(Category category);

    void delete(Long id);
}
