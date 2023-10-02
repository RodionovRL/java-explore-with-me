package ru.practicum.main.service.events.service;

import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.utils.EventState;
import ru.practicum.main.service.events.dto.*;
import ru.practicum.main.service.utils.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    EventFullDto addEventPrivate(long userId, NewEventDto newEventDto);

    Collection<EventShortDto> getAllUsersEventsPrivate(long userId, int from, int size);

    EventFullDto getEventByIdPrivate(long userId, long eventId);

    EventFullDto updateEventPrivate(long userId, long eventId, UpdateEventUserRequest newEventDto);

    Collection<EventFullDto> getEventsForAdmin(List<Long> userIds, List<EventState> states, List<Long> categoryIds,
                                               LocalDateTime start, LocalDateTime end, int from, int size);

    EventFullDto updateEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminDto);

    Collection<EventShortDto> findEventsPublic(
            String text, List<Long> categoryIds, Boolean paid, LocalDateTime start, LocalDateTime end,
            Boolean onlyAvailable, EventSort eventSort, int from, int size, HttpServletRequest request);

    Collection<ParticipationRequestDto> getUserEventRequestsPrivate(long userId, long eventId);

    EventRequestStatusUpdateResult updateEventRequestsStatusPrivate(
            long userId, long eventId, EventRequestStatusUpdateRequest statusUpdateRequest);

    EventFullDto getEventByIdPublic(Long id, HttpServletRequest request);
}
