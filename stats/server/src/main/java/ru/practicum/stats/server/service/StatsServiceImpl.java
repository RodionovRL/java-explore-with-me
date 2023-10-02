package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.maper.StatsMapper;
import ru.practicum.stats.server.model.EndpointHit;
import ru.practicum.stats.server.model.ViewStats;
import ru.practicum.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    @Override
    public void addHitStats(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statsMapper.toEndpointHit(endpointHitDto);
        EndpointHit savedEndpointHit = statsRepository.save(endpointHit);
        log.info("StatsService: added endpointHit={}", savedEndpointHit);
    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUniq) {
        List<ViewStatsDto> resultViewStatsDtoList;
        List<ViewStats> viewStats;
        if (uris != null && !uris.isEmpty()) {
            if (isUniq) {
                viewStats = statsRepository.findStatsByUrisUniqueIp(uris, start, end);
            } else {
                viewStats = statsRepository.findStatsByUrisNotUniqueIp(uris, start, end);
            }
        } else {
            if (isUniq) {
                viewStats = statsRepository.findStatsAllUrisUniqueIp(start, end);
            } else {
                viewStats = statsRepository.findStatsAllUrisNotUniqueIp(start, end);
            }
        }

        log.info("StatsService: returned all {} viewStats", viewStats.size());
        resultViewStatsDtoList = statsMapper.toViewStatsDtoList(viewStats);
        return resultViewStatsDtoList;
    }
}
