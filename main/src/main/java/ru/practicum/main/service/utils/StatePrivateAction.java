package ru.practicum.main.service.utils;

import java.util.Optional;

public enum StatePrivateAction {
    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static Optional<StatePrivateAction> from(String stringState) {
        for (StatePrivateAction action : values()) {
            if (action.name().equalsIgnoreCase(stringState)) {
                return Optional.of(action);
            }
        }
        return Optional.empty();
    }
}
