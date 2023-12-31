package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET '/compilations' получить все компиляции с параметрами: pinned={}, from={}, size={}",
                pinned, from, size);
        return compilationService.getCompilationsByPinned(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("GET '/compilations/{}' получить все компиляции с id={}", compId, compId);
        return compilationService.getCompilationById(compId);
    }
}
