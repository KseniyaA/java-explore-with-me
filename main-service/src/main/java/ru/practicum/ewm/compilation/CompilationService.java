package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDtoRequest;

import java.util.List;

public interface CompilationService {
    Compilation add(Compilation compilation);

    Compilation getById(long compId);

    List<Compilation> getAll(Boolean pinned, Integer from, Integer size);

    void delete(Long compId);

    Compilation update(Long compId, CompilationDtoRequest dtoRequest);
}
