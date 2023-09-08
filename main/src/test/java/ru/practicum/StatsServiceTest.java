package ru.practicum;

import org.junit.jupiter.api.Test;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class StatsServiceTest {

    StatsService statsService;

    String app = "ewm-main-service.test";
    String uri = "/events/test";
    String ip = "121.0.0.1";
    LocalDateTime timestamp =
            LocalDateTime.parse("2023-09-07 13:13:55", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    LocalDateTime start =
            LocalDateTime.parse("2022-09-07 13:13:55", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    LocalDateTime end =
            LocalDateTime.parse("2024-09-07 13:13:55", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    EndpointHitDto endpointHitDto = EndpointHitDto.builder()
            .app(app)
            .uri(uri)
            .ip(ip)
            .timestamp(timestamp)
            .build();

    @Test
    void addEndpointHitAndGetViewStats() {
        List<ViewStatsDto> list = statsService.addEndpointHitAndGetViewStats(endpointHitDto, start, end, List.of(uri), false);
    }
}