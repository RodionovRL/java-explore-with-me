package ru.practicum.main.service.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.service.category.dto.CategoryDto;
import ru.practicum.main.service.utils.EventState;
import ru.practicum.main.service.location.model.Location;
import ru.practicum.main.service.user.dto.UserShotDto;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private int id;
    private UserShotDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private EventState state;
    private boolean requestModeration;
    private String title;
    private int views;
}
