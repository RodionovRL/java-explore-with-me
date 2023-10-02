package ru.practicum.main.service.events.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.exception.InvalidDateRangeException;
import ru.practicum.main.service.utils.EventState;
import ru.practicum.main.service.events.dto.EventFullDto;
import ru.practicum.main.service.events.dto.UpdateEventAdminRequest;
import ru.practicum.main.service.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_FROM;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_SIZE;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventsAdminController {
    private final EventService eventService;

    @GetMapping()
    public ResponseEntity<Collection<EventFullDto>> getEventsForAdmin(
            @RequestParam(name = "users", required = false) List<Long> userIds,
            @RequestParam(name = "states", required = false) List<EventState> states,
            @RequestParam(name = "categories", required = false) List<Long> categoryIds,
            @RequestParam(name = "rangeStart", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(name = "rangeEnd", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(name = "from", required = false, defaultValue = DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) @Positive int size
    ) {
        log.info("EventControllerAdmin: receive GET request users={}, states={}, categories={}, rangeStart={}," +
                " rangeEnd={}, from={}, size={}", userIds, states, categoryIds, start, end, from, size);

        checkStartEndRanges(start, end);

        Collection<EventFullDto> eventsForAdmin =
                eventService.getEventsForAdmin(userIds, states, categoryIds, start, end, from, size);
        return new ResponseEntity<>(eventsForAdmin, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAdmin(
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminDto,
            @PathVariable(name = "eventId") @Positive long eventId
    ) {
        log.info("receive PATCH admin request for update event with eventId={}, with body={}",
                eventId, updateEventAdminDto);
        EventFullDto eventFullDto = eventService.updateEventAdmin(eventId, updateEventAdminDto);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    private void checkStartEndRanges(LocalDateTime start, LocalDateTime end) {
        if (end != null && start != null && end.isBefore(start)) {
            throw new InvalidDateRangeException("Date range is invalid");
        }
    }
}
