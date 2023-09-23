package ru.practicum.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class EndpointHitDto {

    private Long id;

    @NotBlank(message = "App имя не может быть пустым или равное null")
    private String app;

    @NotBlank(message = "URI не может быть пустым или равное null")
    private String uri;

    @NotBlank(message = "IP не может быть пустым или равное null")
    private String ip;

    @NotNull(message = "Timestamp не может быть пустым или равное null")
    private String timestamp;
}
