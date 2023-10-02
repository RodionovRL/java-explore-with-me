package ru.practicum.main.service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.events.model.Event;
import ru.practicum.main.service.events.repository.EventRepository;
import ru.practicum.main.service.exception.InitiatorException;
import ru.practicum.main.service.exception.LimitException;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.exception.NotPublishedException;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.dto.mapper.RequestMapper;
import ru.practicum.main.service.request.model.ParticipationRequest;
import ru.practicum.main.service.request.repository.api.RequestRepository;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.api.UserRepository;
import ru.practicum.main.service.utils.EventState;
import ru.practicum.main.service.utils.RequestState;

import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Transactional
    @Override
    public ParticipationRequestDto addRequest(long userId, long eventId) {
        ParticipationRequest newParticipationRequest = requestRepository.save(prepareRequest(userId, eventId));
        log.info("requestService: was add participationRequest={}", newParticipationRequest);
        return requestMapper.toParticipationRequestDto(newParticipationRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getRequestsPrivate(long userId) {
        User user = findUserById(userId);
        List<ParticipationRequest> requestsList =
                requestRepository.findAllByRequesterId(userId).orElse(Collections.emptyList());
        log.info("requestService: returned private requests list size={}, for user={}", requestsList.size(), user);
        return requestMapper.toDtoList(requestsList);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelOwnRequest(long userId, long requestId) {
        ParticipationRequest request = findRequestByIdAndRequesterId(requestId, userId);
        request.setStatus(RequestState.CANCELED);
        ParticipationRequest canceledRequest = requestRepository.save(request);
        log.info("requestService: request={} was canceled", canceledRequest);
        return requestMapper.toParticipationRequestDto(canceledRequest);
    }


    private ParticipationRequest prepareRequest(long userId, long eventId) {
        User requester = findUserById(userId);
        Event event = findEventById(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new InitiatorException("Requester can not be initiator.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotPublishedException("Event must be published");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new LimitException(String.format("Event is full. ConfirmedRequests >= ParticipantLimit == %d",
                    event.getParticipantLimit()));
        }

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(now())
                .eventId(eventId)
                .requesterId(userId)
                .status(RequestState.PENDING)
                .build();

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return participationRequest;
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private ParticipationRequest findRequestByIdAndRequesterId(long requestId, long requesterId) {
        return requestRepository.findByIdAndRequesterId(requestId, requesterId).orElseThrow(() ->
                new NotFoundException(String.format("Request with id=%d with requesterId=%d was not found",
                        requestId, requesterId)));
    }
}
