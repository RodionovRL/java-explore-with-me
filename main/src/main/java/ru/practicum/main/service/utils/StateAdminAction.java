package ru.practicum.main.service.utils;

import java.util.Optional;

public enum StateAdminAction {
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static Optional<StateAdminAction> from(String stringState) {
        for (StateAdminAction action : values()) {
            if (action.name().equalsIgnoreCase(stringState)) {
                return Optional.of(action);
            }
        }
        return Optional.empty();
    }
}
