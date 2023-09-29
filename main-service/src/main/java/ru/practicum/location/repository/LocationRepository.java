package ru.practicum.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.location.model.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findFirstByLatAndLon(Float lat, Float lon);
}
