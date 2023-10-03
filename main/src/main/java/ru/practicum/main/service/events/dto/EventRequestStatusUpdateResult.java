package ru.practicum.main.service.events.dto;

import lombok.*;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
