package ru.practicum.stats.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatsClient {
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final WebClient client;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String statsServerUrl) {
        client = WebClient.create(statsServerUrl);
    }

    public List<ViewStatsDto> getViewStats(
            LocalDateTime start,
            LocalDateTime end,
            List<Long> ids,
            boolean isUniq
    ) {
        Map<String, Object> parameters = prepareParameters(start, end, ids, isUniq);
        ResponseEntity<List<ViewStatsDto>> result = getStats(parameters);
        if (result.getStatusCode().is5xxServerError()) {
            log.error("Stats-Server error. ErrorStatus={}", result.getStatusCode());
            throw new RuntimeException("Stats-Server error");
        }
        List<ViewStatsDto> stats = Collections.emptyList();
        if (result.getStatusCode().is2xxSuccessful()) {
            stats = result.getBody();
        }
        log.info("Stats client get stats={}", stats);
        return stats;
    }

    private ResponseEntity<List<ViewStatsDto>> getStats(Map<String, Object> parameters) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", parameters.get("start"))
                        .queryParam("end", parameters.get("end"))
                        .queryParam("uris", parameters.get("uris"))
                        .queryParam("unique", parameters.get("isUniq"))
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(ViewStatsDto.class)
                .block();
    }

    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        ResponseEntity<Void> result = postHit(endpointHitDto);
        if (result.getStatusCode().is5xxServerError()) {
            log.error("Stats-Server error. ErrorStatus={}", result.getStatusCode());
            throw new RuntimeException(
                    String.format("Stats-Server error. ErrorStatus=%s", result.getStatusCode()));
        }
    }

    private ResponseEntity<Void> postHit(EndpointHitDto endpointHitDto) {
        return client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(endpointHitDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private Map<String, Object> prepareParameters(
            LocalDateTime start,
            LocalDateTime end,
            List<Long> ids,
            boolean isUniq
    ) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("start", start.format(format));
        parameters.put("end", end.format(format));
        if (ids != null) {
            List<String> uris = ids.stream()
                    .map(id -> "/events/" + id)
                    .collect(Collectors.toList());
            parameters.put("uris", String.join(",", uris));
        }
        parameters.put("isUniq", isUniq);
        return parameters;
    }


}


