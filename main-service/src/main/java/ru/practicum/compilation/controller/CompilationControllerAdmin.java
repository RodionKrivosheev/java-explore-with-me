package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping
@AllArgsConstructor
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Запрос POST для '/admin/compilations' чтобы создать компиляцию с body={}", newCompilationDto.toString());
        return compilationService.createCompilation(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Запрос PATCH для '/admin/compilations/{}' чтобы обновить компиляцию с id={} с body={}",
                compId, compId, updateCompilationRequest.toString());
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Запрос DELETE для '/admin/compilations/{}' чтобы удалить компиляцию с id={}", compId, compId);
        compilationService.deleteCompilation(compId);
    }
}
