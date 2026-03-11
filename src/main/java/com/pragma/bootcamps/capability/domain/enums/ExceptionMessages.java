package com.pragma.bootcamps.capability.domain.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    CAPABILITY_TECHNOLOGIES_COUNT_INVALID("A capability must have between 3 and 20 associated technologies"),
    CAPABILITY_TECHNOLOGIES_REPEATED("There are repeated technologies in the list"),
    TECHNOLOGY_NOT_FOUND("Some technology IDs do not exist"),
    SAGA_COMPENSATION_ASSOCIATION_FAILURE("The capability could not be associated with the selected technologies. Please try again later."),
    CAPABILITY_ALREADY_EXISTS("A capability with the name '%s' already exists");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

}
