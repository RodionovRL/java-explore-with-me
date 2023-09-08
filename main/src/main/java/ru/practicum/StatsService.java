package ru.practicum;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.client.api.StatsClientApi;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@Service
public class StatsService {
    private static StatsClientApi statsClient;

    public List<ViewStatsDto> addEndpointHitAndGetViewStats(
            EndpointHitDto endpointHitDto,
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean isUniq) {
        statsClient.addEndpointHit(endpointHitDto);
        Object viewStats = statsClient.getViewStats(start, end, uris, isUniq);
        List<ViewStatsDto> list = (List<ViewStatsDto>) viewStats;
        return list;
    }

}
