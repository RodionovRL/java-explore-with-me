package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void addHitStats(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUniq);
}
