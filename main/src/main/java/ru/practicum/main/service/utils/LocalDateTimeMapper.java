package ru.practicum.main.service.utils;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public class LocalDateTimeMapper {

    public String asString(LocalDateTime date) {
        if (date != null) {
            return date.toString().replace("T", " ");
        }
        return null;
    }
}
