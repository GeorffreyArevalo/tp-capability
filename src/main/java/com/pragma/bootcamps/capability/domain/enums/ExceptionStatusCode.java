package com.pragma.bootcamps.capability.domain.enums;

public enum ExceptionStatusCode {

    BAD_REQUEST("04-BD"),
    FIELDS_BAD_REQUEST("04-BD-FIELDS"),
    NOT_FOUND("04-NF"),
    CREATED("02-CR"),
    INTERNAL_SERVER_ERROR("05-ISE"),
    FORBIDDEN("04-FB"),
    OK("02-OK"),
    CONFLICT("04-CF");

    private final String statusCode;

    ExceptionStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String status() {
        return statusCode;
    }

}
