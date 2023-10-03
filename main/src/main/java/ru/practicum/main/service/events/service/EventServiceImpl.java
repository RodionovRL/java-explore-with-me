package ru.practicum.main.service.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.category.repository.api.CategoryRepository;
import ru.practicum.main.service.events.dto.*;
import ru.practicum.main.service.events.dto.mapper.EventMapper;
import ru.practicum.main.service.events.model.Event;
import ru.practicum.main.service.events.repository.EventRepository;
import ru.practicum.main.service.exception.LimitException;
import ru.practicum.main.service.exception.NotChangeableException;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.location.model.Location;
import ru.practicum.main.service.location.repository.LocationRepository;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.dto.mapper.RequestMapper;
import ru.practicum.main.service.request.model.ParticipationRequest;
import ru.practicum.main.service.request.repository.api.RequestRepository;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.api.UserRepository;
import ru.practicum.main.service.utils.EventSort;
import ru.practicum.main.service.utils.EventState;
import ru.practicum.main.service.utils.PageRequestUtil;
import ru.practicum.main.service.utils.RequestState;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final LocalDateTime DEFAULT_START =
            LocalDateTime.parse("1996-01-21 00:00:01", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final StatsClient statsClient;

    @Transactional
    @Override
    public EventFullDto addEventPrivate(long userId, NewEventDto newEventDto) {
        User initiator = findUserById(userId);
        LocalDateTime createdOn = now();
        Category category = findCategoryById(newEventDto.getCategoryId());
        Location location = checkLocation(newEventDto.getLocation());
        Event newEvent = eventMapper.toEvent(newEventDto);
        newEvent.setInitiator(initiator);
        newEvent.setCategory(category);
        newEvent.setLocation(location);
        newEvent.setCreatedOn(createdOn);
        newEvent.setPublishedOn(createdOn);
        newEvent.setState(EventState.PENDING);
        Event addedEvent = eventRepository.save(newEvent);
        log.info("eventService: was add event={}", addedEvent);
        return eventMapper.toEventFullDto(addedEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getAllUsersEventsPrivate(long userId, int from, int size) {
        findUserById(userId);
        Pageable pageable = PageRequestUtil.of(from, size);
        List<Event> events = eventRepository.findAllByInitiator_Id(userId, pageable);
        log.info("eventService: returned all {} events", events.size());
        return eventMapper.toEventsShortDtoList(events);
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventByIdPrivate(long userId, long eventId) {
        findUserById(userId);
        Event event = findEventByInitiatorIdAndEventId(userId, eventId);
        log.info("eventService: returned event={}", event);
        return eventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEventPrivate(long userId, long eventId, UpdateEventUserRequest updateEventPrivateDto) {
        findUserById(userId);
        Event oldEvent = findEventByInitiatorIdAndEventId(userId, eventId);
        if (oldEvent.getState() == EventState.PUBLISHED) {
            throw new NotChangeableException("Event must not be published");
        }
        Event newEvent = startEventUpdate(oldEvent, eventMapper.toEvent(updateEventPrivateDto),
                updateEventPrivateDto.getAnnotation(), updateEventPrivateDto.getCategoryId(),
                updateEventPrivateDto.getDescription(), updateEventPrivateDto.getEventDate(),
                updateEventPrivateDto.getLocation(), updateEventPrivateDto.getPaid(),
                updateEventPrivateDto.getParticipantLimit());
        if (updateEventPrivateDto.getStateAction() == null) {
            newEvent.setState(oldEvent.getState());
        } else {
            switch (updateEventPrivateDto.getStateAction()) {
                case CANCEL_REVIEW: {
                    newEvent.setState(EventState.CANCELED);
                    break;
                }
                case SEND_TO_REVIEW: {
                    newEvent.setState(EventState.PENDING);
                    break;
                }
            }
        }
        Event updatedEvent = finalUpdateEvent(oldEvent, newEvent, updateEventPrivateDto.getRequestModeration(), updateEventPrivateDto.getTitle());
        log.info("eventService: update event with id={}, initiatorId={}, oldEvent={}, updatedEvent={}",
                eventId, userId, oldEvent, updatedEvent);
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ParticipationRequestDto> getUserEventRequestsPrivate(long userId, long eventId) {
        Event event = findEventByInitiatorIdAndEventId(userId, eventId);
        List<ParticipationRequest> requests =
                requestRepository.findAllByEventId(eventId).orElse(Collections.emptyList());
        log.info("eventService: returned all {} requests for event={}", requests.size(), event);
        return requestMapper.toDtoList(requests);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateEventRequestsStatusPrivate(
            long userId,
            long eventId,
            EventRequestStatusUpdateRequest statusUpdateRequest) {
        Event event = findEventByInitiatorIdAndEventId(userId, eventId);
        EventRequestStatusUpdateResult statusUpdateResult = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(Collections.emptyList())
                .rejectedRequests(Collections.emptyList())
                .build();
        List<ParticipationRequest> requests =
                findPendingRequests(eventId, statusUpdateRequest.getRequestIds());

        if (!event.isRequestModeration()) {
            statusUpdateResult.setConfirmedRequests(requestMapper.toDtoList(confirmRequests(requests, event)));
            return statusUpdateResult;
        }
        if (statusUpdateRequest.getStatus() == RequestState.REJECTED) {
            List<ParticipationRequest> rejectedRequests = rejectRequests(requests);
            statusUpdateResult.setRejectedRequests(requestMapper.toDtoList(rejectedRequests));
            return statusUpdateResult;
        }
        if (event.getParticipantLimit() == 0) {
            statusUpdateResult.setConfirmedRequests(requestMapper.toDtoList(confirmRequests(requests, event)));
            return statusUpdateResult;
        }

        int numFree = getNumFreePlaces(event);
        List<ParticipationRequest> requestForConfirm;
        List<ParticipationRequest> requestsForReject;
        requestForConfirm = requests.stream()
                .limit(numFree)
                .collect(Collectors.toList());

        statusUpdateResult.setConfirmedRequests(
                requestMapper.toDtoList(confirmRequests(requestForConfirm, event)));

        requestsForReject = requests.stream()
                .skip(numFree)
                .collect(Collectors.toList());

        statusUpdateResult.setRejectedRequests(
                requestMapper.toDtoList(rejectRequests(requestsForReject)));


        return statusUpdateResult;
    }

    @Transactional
    @Override
    public EventFullDto updateEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminDto) {
        Event oldEvent = findEventById(eventId);
        Event newEvent = startEventUpdate(oldEvent, eventMapper.toEvent(updateEventAdminDto),
                updateEventAdminDto.getAnnotation(), updateEventAdminDto.getCategoryId(),
                updateEventAdminDto.getDescription(), updateEventAdminDto.getEventDate(),
                updateEventAdminDto.getLocation(), updateEventAdminDto.getPaid(),
                updateEventAdminDto.getParticipantLimit());

        if (updateEventAdminDto.getStateAction() == null) {
            newEvent.setState(oldEvent.getState());
        } else {
            switch (updateEventAdminDto.getStateAction()) {
                case PUBLISH_EVENT: {
                    if (oldEvent.getState() == (EventState.PUBLISHED)) {
                        throw new NotChangeableException("Event must not be published");
                    }
                    if (oldEvent.getState() == (EventState.CANCELED)) {
                        throw new NotChangeableException("Event must not be canceled");
                    }
                    newEvent.setState(EventState.PUBLISHED);
                    break;
                }
                case REJECT_EVENT: {
                    if (oldEvent.getState() == (EventState.PUBLISHED)) {
                        throw new NotChangeableException("Event must not be published");
                    }
                    newEvent.setState(EventState.CANCELED);
                    break;
                }
            }
        }
        Event updatedEvent = finalUpdateEvent(oldEvent, newEvent, updateEventAdminDto.getRequestModeration(),
                updateEventAdminDto.getTitle());
        log.info("eventService: admin update event with id={}, oldEvent={}, updatedEvent={}",
                eventId, oldEvent, updatedEvent);
        return eventMapper.toEventFullDto(updatedEvent);
    }

    private Event startEventUpdate(
            Event oldEvent, Event newEvent, String annotation, Long categoryId, String description,
            LocalDateTime eventDate, Location location, Boolean paid, Integer participantLimit
    ) {
        newEvent.setId(oldEvent.getId());
        if (annotation == null) {
            newEvent.setAnnotation(oldEvent.getAnnotation());
        }
        if (categoryId == null) {
            newEvent.setCategory(oldEvent.getCategory());
        }
        newEvent.setConfirmedRequests(oldEvent.getConfirmedRequests());
        newEvent.setCreatedOn(oldEvent.getCreatedOn());
        if (description == null) {
            newEvent.setDescription(oldEvent.getDescription());
        }
        if (eventDate == null) {
            newEvent.setEventDate(oldEvent.getEventDate());
        }
        newEvent.setInitiator(oldEvent.getInitiator());
        if (location == null) {
            newEvent.setLocation(oldEvent.getLocation());
        }
        if (paid == null) {
            newEvent.setPaid(oldEvent.isPaid());
        }
        if (participantLimit == null) {
            newEvent.setParticipantLimit(oldEvent.getParticipantLimit());
        }
        newEvent.setPublishedOn(oldEvent.getPublishedOn());
        return newEvent;
    }

    private Event finalUpdateEvent(Event oldEvent, Event newEvent, Boolean requestModeration, String title) {
        if (requestModeration == null) {
            newEvent.setRequestModeration(oldEvent.isRequestModeration());
        }
        if (title == null) {
            newEvent.setTitle(oldEvent.getTitle());
        }
        newEvent.setViews(oldEvent.getViews());
        return eventRepository.save(newEvent);
    }

    @Transactional
    @Override
    public List<EventFullDto> getEventsForAdmin(
            List<Long> userIds, List<EventState> states, List<Long> categoryIds,
            LocalDateTime start, LocalDateTime end, int from, int size
    ) {
        Pageable pageable = PageRequestUtil.of(from, size);
        if (start == null) {
            start = now();
        }
        List<Event> eventsForAdmin =
                eventRepository.findEventForAdmin(userIds, states, categoryIds, start, end, pageable)
                        .orElse(Collections.emptyList());
        log.info("eventService: for admin returned {} events", eventsForAdmin.size());
        return eventMapper.toEventsFullDtoList(eventsForAdmin);
    }


    @Transactional
    @Override
    public Collection<EventShortDto> findEventsPublic(
            String text, List<Long> categoryIds, Boolean paid, LocalDateTime start, LocalDateTime end,
            Boolean onlyAvailable, EventSort eventSort, int from, int size, HttpServletRequest request
    ) {
        if (start == null) {
            start = now();
        }

        Pageable pageable = PageRequestUtil.of(from, size);
        if (eventSort == EventSort.EVENT_DATE) {
                pageable = PageRequestUtil.of(from, size, Sort.by("eventDate"));
        }

        List<Event> foundEvents = eventRepository
                .findEventPublic(EventState.PUBLISHED, text, categoryIds, start, end, onlyAvailable, pageable)
                .orElse(Collections.emptyList());

        if (eventSort == EventSort.VIEWS) {
            foundEvents = foundEvents.stream()
                    .sorted(Event::compareTo)
                    .collect(Collectors.toList());
        }

        List<Long> eventsIds = foundEvents.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        List<ViewStatsDto> viewStats = statsClient.getViewStats(
                DEFAULT_START,
                now().plusHours(1),
                eventsIds,
                true
        );

        foundEvents.forEach(e -> viewStats.stream()
                .filter(s -> Long.parseLong(
                        s.getUri().substring(s.getUri().lastIndexOf("/") + 1)) == e.getId())
                .findFirst()
                .ifPresent(s -> e.setViews(s.getHits())));

        eventRepository.saveAll(foundEvents);

        statsClient.addEndpointHit(EndpointHitDto.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(now())
                .build()
        );

        return eventMapper.toEventsShortDtoList(foundEvents);
    }

    @Transactional
    @Override
    public EventFullDto getEventByIdPublic(Long id, HttpServletRequest request) {
        Event event = findEventById(id);
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException(String.format("Published event with id=%d not found", id));
        }
        List<ViewStatsDto> viewStats = statsClient.getViewStats(
                DEFAULT_START,
                now().plusHours(1).truncatedTo(ChronoUnit.SECONDS),
                List.of(id),
                true);

        viewStats.stream()
                .filter(s -> Long.parseLong(
                        s.getUri().substring(s.getUri().lastIndexOf("/") + 1)) == event.getId())
                .findFirst()
                .ifPresent(s -> event.setViews(s.getHits()));

        eventRepository.save(event);

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("main-service")
                .uri("/events/" + id)
                .ip(request.getRemoteAddr())
                .timestamp(now())
                .build();
        statsClient.addEndpointHit(endpointHitDto);

        return eventMapper.toEventFullDto(event);
    }

    private int getNumFreePlaces(Event event) {
        int numFree = event.getParticipantLimit() - event.getConfirmedRequests();
        if (numFree <= 0) {
            throw new LimitException("The participant limit has been reached");
        }
        return numFree;
    }

    private List<ParticipationRequest> findPendingRequests(Long eventId, List<Long> requestsIds) {
        List<ParticipationRequest> requests =
                requestRepository.findAllByEventIdAndIdIn(eventId, requestsIds).orElseThrow(() ->
                        new NotFoundException(String.format("requests with ids=%s for eventId=%d not found",
                                requestsIds, eventId)));
        requests.stream()
                .filter(r -> RequestState.PENDING != (r.getStatus()))
                .findFirst()
                .ifPresent(r -> {
                    throw new NotChangeableException("All Request must have status PENDING");
                });
        return requests;
    }

    private List<ParticipationRequest> rejectRequests(List<ParticipationRequest> requests) {
        List<ParticipationRequest> requestsForCancel = requests.stream()
                .filter(r -> r.getStatus() == (RequestState.PENDING))
                .peek(r -> r.setStatus(RequestState.REJECTED))
                .collect(Collectors.toList());

        return requestRepository.saveAll(requestsForCancel);
    }

    private List<ParticipationRequest> confirmRequests(List<ParticipationRequest> requests, Event event) {
        List<ParticipationRequest> requestsForConfirm = requests.stream()
                .filter(r -> r.getStatus() == (RequestState.PENDING))
                .peek(r -> r.setStatus(RequestState.CONFIRMED))
                .collect(Collectors.toList());
        event.setConfirmedRequests(event.getConfirmedRequests() + requestsForConfirm.size());
        eventRepository.save(event);
        return requestRepository.saveAll(requestsForConfirm);
    }

    private Event findEventByInitiatorIdAndEventId(long userId, long eventId) {
        return eventRepository.findByInitiator_IdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException(String
                        .format("Event with id=%d and initiatorId=%d was not found", eventId, userId)));
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Category findCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%d was not found", categoryId)));
    }

    private Location checkLocation(Location location) {
        float lat = location.getLat();
        float lon = location.getLon();
        Optional<Location> foundLocation = locationRepository.findByLatAndLon(lat, lon);
        if (foundLocation.isEmpty()) {
            return locationRepository.save(new Location(null, lat, lon));
        }
        return foundLocation.get();
    }
}
