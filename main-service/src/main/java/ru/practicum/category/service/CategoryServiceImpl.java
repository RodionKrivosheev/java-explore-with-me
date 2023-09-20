package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.entity.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.events.model.entity.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.util.pageable.OffsetPageRequest;
import ru.practicum.util.validation.SizeValidator;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.info("Создается категория с параметром body={}", newCategoryDto.toString());
        Category category = CategoryMapper.toCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        SizeValidator.validateSize(size);
        Pageable pageable = OffsetPageRequest.of(from, size);

        log.info("Получение всех категорий: from={}, size={}", from, size);
        List<Category> categories = categoryRepository.findAll(pageable).getContent();

        return CategoryMapper.toCategoryDto(categories);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        log.info("Получение категории для id={}", id);
        Category category = getCategoryModelById(id);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public Category getCategoryModelById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Category", id));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = getCategoryModelById(id);

        log.info("Обновление категории с помощью id={}", id);
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryExists(id);
        Event event = eventRepository.findFirstByCategoryId(id);

        if (event != null) {
            throw new ConflictException("The category is not empty.");
        }

        log.info("Удаление категории с помощью id={}", id);
        categoryRepository.deleteById(id);
    }

    @Override
    public void categoryExists(Long id) {
        log.info("Проверка существования категории с помощью id={}", id);

        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category", id);
        }
    }
}
