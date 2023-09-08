package ru.practicum.client.api;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClientApi {
    ResponseEntity<Object> addEndpointHit(EndpointHitDto endpointHitDto);

    ResponseEntity<Object> getViewStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean isUniq
    );

    ResponseEntity<Object> getViewStats(
            LocalDateTime start,
            LocalDateTime end,
            boolean isUniq
    );
}