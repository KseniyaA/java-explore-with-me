package ru.practicum.ewm.category.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.category.dto.CategoryDtoRequest;
import ru.practicum.ewm.category.dto.CategoryDtoResponse;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryDtoResponse add(@RequestBody @Valid CategoryDtoRequest dto) {
        log.info("Получен запрос POST /admin/categories с параметрами dto = {}", dto);
        Category category = categoryService.add(CategoryMapper.toCategory(dto));
        return CategoryMapper.toCategoryDtoResponse(category);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoryDtoResponse update(@PathVariable("catId") long catId, @RequestBody @Valid CategoryDtoRequest dto) {
        log.info("Получен запрос PATCH /admin/categories/{catId} с параметрами catId = {}, dto = {}", catId, dto);
        Category category = categoryService.update(CategoryMapper.toCategory(dto, catId));
        return CategoryMapper.toCategoryDtoResponse(category);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("catId") long catId) {
        log.info("Получен запрос DELETE /admin/categories/{catId} с параметрами catId = {}", catId);
        categoryService.delete(catId);
    }
}
