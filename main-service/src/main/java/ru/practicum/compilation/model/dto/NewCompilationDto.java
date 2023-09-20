package ru.practicum.compilation.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;
    private Boolean pinned;

    @NotBlank
    @Size(max = 50, min = 1)
    private String title;
}
