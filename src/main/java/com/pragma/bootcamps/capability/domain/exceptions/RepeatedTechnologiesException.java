package com.pragma.bootcamps.capability.domain.exceptions;

import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;

public class RepeatedTechnologiesException extends CapabilityException {
    public RepeatedTechnologiesException(String message) {
        super(ExceptionStatusCode.CONFLICT, message, 409);
    }
}
