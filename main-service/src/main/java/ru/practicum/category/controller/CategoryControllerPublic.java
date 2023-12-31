package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.model.dto.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryControllerPublic {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(
            @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET '/categories' получает все категории с параметрами: from={}, size={}", from, size);
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("GET '/categories/{}' получает категорию с id={}", catId, catId);
        return categoryService.getCategoryById(catId);
    }
}
