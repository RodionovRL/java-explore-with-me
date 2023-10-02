package ru.practicum.main.service.events.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.events.dto.EventFullDto;
import ru.practicum.main.service.events.dto.EventShortDto;
import ru.practicum.main.service.events.service.EventService;
import ru.practicum.main.service.exception.InvalidDateRangeException;
import ru.practicum.main.service.utils.EventSort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_FROM;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_SIZE;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> findEventsPublic(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categoryIds,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(name = "rangeEnd", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(name = "onlyAvailable", defaultValue = "false", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE", required = false) EventSort eventSort,
            @RequestParam(value = "from", defaultValue = DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) @Positive int size,
            HttpServletRequest request
    ) {
        log.info("receive GET to find events by " +
                        "text={}, categories={}, paid={}, start={}, end={}, onlyAvailable={}, eventSort={}, from={}, size={}",
                text, categoryIds, paid, start, end, onlyAvailable, eventSort, from, size);
        log.info("HttpServletRequest: remoteAddr={}, requestURI={}", request.getRemoteAddr(), request.getRequestURI());

        checkStartEndRanges(start, end);

        Collection<EventShortDto> eventsShortDto = eventService.findEventsPublic(
                text, categoryIds, paid, start, end, onlyAvailable, eventSort, from, size, request);
        return new ResponseEntity<>(eventsShortDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventByIdPublic(
            @PathVariable(value = "id") @Positive Long id,
            HttpServletRequest request

    ) {
        log.info("receive GET to find events by id={}", id);
        log.info("HttpServletRequest: remoteAddr={}, requestURI={}", request.getRemoteAddr(), request.getRequestURI());

        EventFullDto eventFullDto = eventService.getEventByIdPublic(id, request);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    private void checkStartEndRanges(LocalDateTime start, LocalDateTime end) {
        if (end != null && start != null && end.isBefore(start)) {
            throw new InvalidDateRangeException("Date range is invalid");
        }
    }
}
