package ru.practicum.compilation.model.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {

    private Set<Long> events;
    private Boolean pinned;

    @Size(max = 50, min = 1)
    private String title;
}
