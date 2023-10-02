package ru.practicum.main.service.request.service;

import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequestsPrivate(long userId);

    ParticipationRequestDto addRequest(long userId, long eventId);

    ParticipationRequestDto cancelOwnRequest(long userId, long requestId);
}
