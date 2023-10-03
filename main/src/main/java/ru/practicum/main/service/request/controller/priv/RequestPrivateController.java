package ru.practicum.main.service.request.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestService requestService;

    @PostMapping("")
    public ResponseEntity<ParticipationRequestDto> addRequest(
            @PathVariable(value = "userId") @Positive long userId,
            @RequestParam(value = "eventId") @Positive long eventId
    ) {
        log.info("RequestPrivateController: " +
                "receive POST request for add new ParticipationRequest for userId={}, eventId={}", userId, eventId);
        ParticipationRequestDto requestDto = requestService.addRequest(userId, eventId);
        return new ResponseEntity<>(requestDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsPrivate(
            @PathVariable(value = "userId") @Positive long userId
    ) {
        log.info("RequestPrivateController: receive GET request for get ParticipationRequests for userId={}",
                userId);
        List<ParticipationRequestDto> requestsPrivate = requestService.getRequestsPrivate(userId);
        return new ResponseEntity<>(requestsPrivate, HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelOwnRequest(
            @PathVariable(value = "userId") @Positive long userId,
            @PathVariable(value = "requestId") @Positive long requestId
    ) {
        log.info("RequestPrivateController: " +
                "receive PATCH request from userId={} for cancel ParticipationRequest requestId={}", userId, requestId);
        ParticipationRequestDto canceledRequestDto = requestService.cancelOwnRequest(userId, requestId);
        return new ResponseEntity<>(canceledRequestDto, HttpStatus.OK);
    }


}
