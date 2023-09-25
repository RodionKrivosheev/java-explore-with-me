package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class EndpointHitDto {

    private Long id;

    @NotBlank(message = "App Name can not by empty or null")
    private String app;

    @NotBlank(message = "URI can not by empty or null")
    private String uri;

    @NotBlank(message = "IP can not by empty or null")
    private String ip;

    @NotNull(message = "Timestamp can not by empty or null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
