package ru.practicum.location.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class LocationDto {
    private Float lat;
    private Float lon;
}
