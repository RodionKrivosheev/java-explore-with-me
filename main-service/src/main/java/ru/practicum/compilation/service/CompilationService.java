package ru.practicum.compilation.service;

import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.entity.Compilation;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    List<CompilationDto> getCompilationsByPinned(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long id);

    Compilation getCompilationModelById(Long id);

    CompilationDto updateCompilation(Long id, UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilation(Long id);

    void compilationExists(Long id);
}
