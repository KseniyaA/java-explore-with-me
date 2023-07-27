package ru.practicum.ewm.category;

import java.util.List;

public interface CategoryService {
    Category add(Category category);

    Category update(Category category);

    void delete(Long id);

    List<Category> getAll(Integer from, Integer size);

    Category getById(long catId);
}
