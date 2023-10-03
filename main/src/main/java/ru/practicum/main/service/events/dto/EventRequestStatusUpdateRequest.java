package ru.practicum.main.service.events.dto;


import lombok.*;
import ru.practicum.main.service.utils.RequestState;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestState status;
}
