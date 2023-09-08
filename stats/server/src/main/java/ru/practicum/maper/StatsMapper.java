package ru.practicum.maper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    @Mapping(target = "id", ignore = true)
    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);

    ViewStatsDto toViewStatsDto(ViewStats viewStats);

    List<ViewStatsDto> toViewStatsDtoList(List<ViewStats> viewStats);
}
