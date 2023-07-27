package ru.practicum.ewm.compilation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.compilation.CompilationMapper;
import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDtoRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;
import ru.practicum.ewm.valid.Marker;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CompilationDtoResponse add(@RequestBody @Validated(Marker.OnCreate.class) CompilationDtoRequest compilationDtoRequest) {
        log.info("Получен запрос POST /admin/compilations с параметрами dto = {}", compilationDtoRequest);
        Compilation compilation = compilationService.add(CompilationMapper.toCompilation(compilationDtoRequest));
        return CompilationMapper.toCompilationDtoResponse(compilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("compId") Long compId) {
        log.info("Получен запрос DELETE /admin/compilations/{compId} с параметрами compId = {}", compId);
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CompilationDtoResponse update(@PathVariable("compId") Long compId,
                                         @RequestBody @Validated(Marker.OnUpdate.class) CompilationDtoRequest dtoRequest) {
        log.info("Получен запрос PATCH /admin/compilations/{compId} с параметрами compId = {}", compId);
        return CompilationMapper.toCompilationDtoResponse(compilationService.update(compId, dtoRequest));
    }
}
