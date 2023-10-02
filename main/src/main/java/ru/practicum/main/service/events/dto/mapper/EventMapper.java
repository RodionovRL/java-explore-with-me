package ru.practicum.main.service.events.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.main.service.events.dto.*;
import ru.practicum.main.service.events.model.Event;
import ru.practicum.main.service.utils.LocalDateTimeMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = LocalDateTimeMapper.class)
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    Event toEvent(NewEventDto newEventDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    Event toEvent(UpdateEventUserRequest updateEventUserRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    Event toEvent(UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto toEventFullDto(Event event);

    @Named(value = "toEventShortDtoList")
    EventShortDto toEventShortDto(Event event);

    List<EventShortDto> toEventsShortDtoList(List<Event> events);

    List<EventFullDto> toEventsFullDtoList(List<Event> events);

}
