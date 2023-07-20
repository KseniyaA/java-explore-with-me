package ru.practicum.ewm.category.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.category.dto.CategoryDtoRequest;
import ru.practicum.ewm.category.dto.CategoryDtoResponse;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDtoResponse add(@RequestBody CategoryDtoRequest dto) {
        log.info("Получен запрос POST /admin/categories с параметрами dto = {}", dto);
        Category category = categoryService.add(CategoryMapper.toCategory(dto));
        return CategoryMapper.toCategoryDto(category);
    }

    @PatchMapping("/{catId}")
    public CategoryDtoResponse update(@PathVariable("catId") long catId, @RequestBody CategoryDtoRequest dto) {
        log.info("Получен запрос PATCH /admin/categories/{catId} с параметрами catId = {}, dto = {}", catId, dto);
        Category category = categoryService.update(CategoryMapper.toCategory(dto, catId));
        return CategoryMapper.toCategoryDto(category);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable("catId") long catId) {
        log.info("Получен запрос DELETE /admin/categories/{catId} с параметрами catId = {}", catId);
        categoryService.delete(catId);
    }
}
