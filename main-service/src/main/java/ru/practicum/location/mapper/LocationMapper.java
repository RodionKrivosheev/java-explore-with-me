package ru.practicum.location.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.location.model.entity.Location;
import ru.practicum.location.model.dto.LocationDto;

@UtilityClass
public class LocationMapper {

    public LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
