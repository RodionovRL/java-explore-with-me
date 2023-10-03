package ru.practicum.main.service.events.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.events.dto.*;
import ru.practicum.main.service.events.service.EventService;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_FROM;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_SIZE;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventsPrivateController {
    private final EventService eventService;

    @PostMapping("")
    public ResponseEntity<EventFullDto> addEvent(
            @RequestBody @Valid NewEventDto newEventDto,
            @PathVariable(name = "userId") long userId
    ) {
        log.info("EventController: receive POST request from userId={} for add new event with body={}", userId, newEventDto);
        EventFullDto savedEvent = eventService.addEventPrivate(userId, newEventDto);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Collection<EventShortDto>> getAllUsersEvents(
            @PathVariable(name = "userId") long userId,
            @RequestParam(name = "from", required = false, defaultValue = DEFAULT_FROM) int from,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size
    ) {
        log.info("receive GET request for return All events with userId={}, from={}, size={}", userId, from, size);
        Collection<EventShortDto> eventsShortDto = eventService.getAllUsersEventsPrivate(userId, from, size);
        return new ResponseEntity<>(eventsShortDto, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getUsersEventById(
            @PathVariable(name = "userId") @Positive long userId,
            @PathVariable(name = "eventId") @Positive long eventId
    ) {
        log.info("receive GET request for return event with eventId={}, for userId={}", eventId, userId);
        EventFullDto eventFullDto = eventService.getEventByIdPrivate(userId, eventId);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateUsersEvent(
            @RequestBody @Valid UpdateEventUserRequest updateEventDto,
            @PathVariable(name = "userId") @Positive long userId,
            @PathVariable(name = "eventId") @Positive long eventId
    ) {
        log.info("receive PATCH private request for update event with eventId={}, for userId={}, with body={}",
                eventId, userId, updateEventDto);
        EventFullDto eventFullDto = eventService.updateEventPrivate(userId, eventId, updateEventDto);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Collection<ParticipationRequestDto>> getUserEventRequests(
            @PathVariable(name = "userId") @Positive long userId,
            @PathVariable(name = "eventId") @Positive long eventId
    ) {
        log.info("receive GET request for return requests for event with eventId={}, for userId={}", eventId, userId);
        Collection<ParticipationRequestDto> requestsDto = eventService.getUserEventRequestsPrivate(userId, eventId);
        return new ResponseEntity<>(requestsDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateEventRequestsStatus(
            @PathVariable(name = "userId") @Positive long userId,
            @PathVariable(name = "eventId") @Positive long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest statusUpdateRequest
    ) {
        log.info("receive PATCH private request from userId={} for update eventId={} with updateRequest={}",
                userId, eventId, statusUpdateRequest);
        EventRequestStatusUpdateResult statusUpdateResult = eventService.updateEventRequestsStatusPrivate(
                userId, eventId, statusUpdateRequest);
        return new ResponseEntity<>(statusUpdateResult, HttpStatus.OK);
    }
}
