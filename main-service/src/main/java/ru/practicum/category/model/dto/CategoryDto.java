package ru.practicum.category.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @NotNull
    @Size(max = 50, min = 1)
    private String name;
}
