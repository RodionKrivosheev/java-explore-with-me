package ru.practicum.location;

import ru.practicum.location.model.entity.Location;

public interface LocationService {

    Location getByLatAndLong(Float lat, Float lon);

    Location create(Location location);

    Boolean locationExists(Float lat, Float lon);

    Location getLocation(Float lat, Float lon);
}
