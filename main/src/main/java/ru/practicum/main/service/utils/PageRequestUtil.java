package ru.practicum.main.service.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PageRequestUtil {
    public static PageRequest of(int from, int size, Sort sort) {
        return PageRequest.of(from / size, size, sort);
    }

    public static PageRequest of(int from, int size) {
        return of(from, size, Sort.unsorted());
    }
}
