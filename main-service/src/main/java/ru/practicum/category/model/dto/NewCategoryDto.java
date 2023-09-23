package ru.practicum.category.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    @NotBlank
    @Size(max = 50, min = 1)
    private String name;
}
