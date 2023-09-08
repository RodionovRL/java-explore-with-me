package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.maper.StatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

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
        statsRepository.save(endpointHit);
        log.info("StatsService: added endpointHit={}", endpointHit);
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
