package ru.practicum.stats.server.maper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.EndpointHit;
import ru.practicum.stats.server.model.ViewStats;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    @Mapping(target = "id", ignore = true)
    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);

    EndpointHitDto toEndpointHitDto(EndpointHit endpointHit);

    ViewStatsDto toViewStatsDto(ViewStats viewStats);

    List<ViewStatsDto> toViewStatsDtoList(List<ViewStats> viewStats);
}
