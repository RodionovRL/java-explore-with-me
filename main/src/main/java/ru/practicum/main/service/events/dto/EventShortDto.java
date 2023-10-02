package ru.practicum.main.service.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.service.category.dto.CategoryDto;
import ru.practicum.main.service.user.dto.UserShotDto;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String eventDate;
    private int id;
    private UserShotDto initiator;
    private boolean paid;
    private String title;
    private int views;
}
