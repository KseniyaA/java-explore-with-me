package ru.practicum.ewm.category;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.dto.CategoryDtoRequest;
import ru.practicum.ewm.category.dto.CategoryDtoResponse;

@UtilityClass
public class CategoryMapper {
    public Category toCategory(CategoryDtoRequest dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public Category toCategory(CategoryDtoRequest dto, Long id) {
        return Category.builder()
                .id(id)
                .name(dto.getName())
                .build();
    }

    public CategoryDtoResponse toCategoryDto(Category category) {
        return CategoryDtoResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
