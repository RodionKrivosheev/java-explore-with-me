package ru.practicum.location;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.location.model.entity.Location;
import ru.practicum.location.repository.LocationRepository;

@Slf4j
@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location getByLatAndLong(Float lat, Float lon) {
        log.info("Получение location by lat={} and lon={}", lat, lon);
        return locationRepository.findFirstByLatAndLon(lat, lon);
    }

    @Override
    public Location create(Location location) {
        log.info("Создание location с body={}", location.toString());
        return locationRepository.save(location);
    }

    @Override
    public Boolean locationExists(Float lat, Float lon) {
        log.info("Проверка на существование location with lat={} and lon={}", lat, lon);
        return locationRepository.findFirstByLatAndLon(lat, lon) != null;
    }

    @Override
    public Location getLocation(Float lat, Float lon) {

        if (locationExists(lat, lon)) {
            return getByLatAndLong(lat, lon);
        }

        Location location = Location.builder()
                .lat(lat)
                .lon(lon)
                .build();

        return locationRepository.save(location);
    }
}
