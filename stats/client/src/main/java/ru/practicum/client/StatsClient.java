package ru.practicum.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.api.StatsClientApi;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient implements StatsClientApi {
    private static final String API_PREFIX = "";

    public StatsClient(String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Object> addEndpointHit(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

    @Override
    public ResponseEntity<Object> getViewStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean isUniq
    ) {
        Map<String, Object> parameters = prepareParameters(start, end, uris, isUniq);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={isUniq}", parameters);
    }

    @Override
    public ResponseEntity<Object> getViewStats(
            LocalDateTime start,
            LocalDateTime end,
            boolean isUniq
    ) {
        Map<String, Object> parameters = prepareParameters(start, end, null, isUniq);
        return get("/stats?start={start}&end={end}&unique={isUniq}", parameters);
    }

    private Map<String, Object> prepareParameters(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean isUniq
    ) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.toString().replace("T", " "));
        parameters.put("end", end.toString().replace("T", " "));
        if (uris != null) {
            parameters.put("uris", String.join(",", uris));
        }
        parameters.put("isUniq", isUniq);
        return parameters;
    }
}
