package ru.practicum.main.service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.service.location.model.Location;
import ru.practicum.main.service.utils.AfterDeltaHoursFromNow;
import ru.practicum.main.service.utils.StatePrivateAction;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, message = "Field: annotation. Error: annotation length too short.")
    @Size(max = 2000, message = "Field: annotation. Error: annotation length too long.")
    private String annotation;
    @JsonProperty("category")
    @Positive(message = "Field: categoryId. Error: categoryId must be positive")
    private Long categoryId;
    @Size(min = 20, message = "Field: description. Error: description length too short.")
    @Size(max = 7000, message = "Field: annotation. Error: description length too long.")
    private String description;
    @AfterDeltaHoursFromNow(message = "eventDate must be not before than 2 hours from now", deltaHours = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero(message = "Field: participantLimit. Error: participantLimit must be zero or positive.")
    private Integer participantLimit;
    private Boolean requestModeration;
    private StatePrivateAction stateAction;
    @Size(min = 3, max = 120, message = "Field: title. Error: length error.")
    private String title;
}
