package ru.practicum.events.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.location.model.entity.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotNull
    @Size(max = 2000, min = 20)
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @Size(max = 7000, min = 20)
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;
    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;

    @NotNull
    @Size(max = 120, min = 3)
    private String title;
}
