package ru.practicum.ewm.category.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.category.dto.CategoryDtoResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/categories")
@Slf4j
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CategoryDtoResponse> getAll(@RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /categories с параметрами from = {}, size = {}", from, size);
        List<Category> categories = categoryService.getAll(from, size);
        return categories.stream().map(CategoryMapper::toCategoryDtoResponse).collect(Collectors.toList());
    }

    @GetMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoryDtoResponse get(@PathVariable("catId") long catId) {
        log.info("Получен запрос GET /admin/categories/{catId} с параметрами catId = {}", catId);
        return CategoryMapper.toCategoryDtoResponse(categoryService.getById(catId));
    }

}
