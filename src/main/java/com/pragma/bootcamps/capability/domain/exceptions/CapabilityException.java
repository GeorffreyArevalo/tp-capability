package com.pragma.bootcamps.capability.domain.exceptions;

import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;

public class CapabilityException extends RuntimeException {

    private final ExceptionStatusCode statusCode;
    private final int status;

    public CapabilityException(ExceptionStatusCode statusCode, String message, int status) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }

}
