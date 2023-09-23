package ru.practicum.compilation.model.dto;

import lombok.*;
import ru.practicum.events.model.dto.EventShortDto;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Set<EventShortDto> events;
    private Long id;

    @NotNull
    private Boolean pinned;

    @NotNull
    private String title;
}
