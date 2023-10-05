package ru.practicum.main.service.request.service;

import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequestsPrivate(long userId);

    ParticipationRequestDto addRequestPrivate(long userId, long eventId, boolean subscriptAccept);

    ParticipationRequestDto cancelOwnRequest(long userId, long requestId);

    ParticipationRequestDto requestSubscriptionPermitChange(long userId, long requestId, boolean subscriptionPermit);
}
