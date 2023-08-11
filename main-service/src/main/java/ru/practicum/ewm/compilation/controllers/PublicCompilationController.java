package ru.practicum.ewm.compilation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.compilation.CompilationMapper;
import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/compilations")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDtoResponse> getAll(@PathVariable(value = "pinned", required = false) Boolean pinned,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /compilations с параметрами pinned = {}, from = {}, size = {}",
                pinned, from, size);
        List<Compilation> compilations = compilationService.getAll(pinned, from, size);
        return compilations.stream().map(CompilationMapper::toCompilationDtoResponse).collect(Collectors.toList());
    }

    @GetMapping("/{compId}")
    public CompilationDtoResponse get(@PathVariable("compId") long compId) {
        log.info("Получен запрос GET /compilations/{compId} с параметрами compId = {}", compId);
        return CompilationMapper.toCompilationDtoResponse(compilationService.getById(compId));
    }
}
