package com.pragma.bootcamps.capability.domain.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    CAPABILITY_TECHNOLOGIES_COUNT_INVALID("A capability must have between 3 and 20 associated technologies"),
    CAPABILITY_TECHNOLOGIES_REPEATED("There are repeated technologies in the list"),
    TECHNOLOGY_NOT_FOUND("Some technology IDs do not exist"),
    SAGA_COMPENSATION_ASSOCIATION_FAILURE("The capability could not be associated with the selected technologies. Please try again later."),
    CAPABILITY_ALREADY_EXISTS("A capability with the name '%s' already exists"),
    WEB_CLIENT_INTERNAL_SERVER_ERROR("Internal server error in technology microservice."),
    BOOTCAMP_CAPABILITIES_COUNT_INVALID("A bootcamp must have between 1 and 4 associated capabilities"),
    BOOTCAMP_CAPABILITIES_REPEATED("There are repeated capabilities in the list"),
    CAPABILITY_NOT_FOUND("Some capability IDs do not exist");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

}
