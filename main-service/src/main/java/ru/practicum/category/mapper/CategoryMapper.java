package ru.practicum.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.entity.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CategoryMapper {

    public Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryDto> toCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();

        for (Category c : categories) {
            result.add(toCategoryDto(c));
        }

        return result;
    }
}
