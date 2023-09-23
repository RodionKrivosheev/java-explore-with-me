package ru.practicum.category.repository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.entity.Category;

@ComponentScan
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
