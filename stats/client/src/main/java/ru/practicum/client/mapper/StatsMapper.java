package ru.practicum.client.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    ViewStatsDto toViewStatsDto(Object object);

    List<ViewStatsDto> toViewStatsDtoList(List<Object> objects);
}
