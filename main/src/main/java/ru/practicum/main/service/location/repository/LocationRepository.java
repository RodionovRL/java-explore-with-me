package ru.practicum.main.service.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.location.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLon(float lat, float lon);
}
