package com.pragma.bootcamps.capability.domain.exceptions;

import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;

public class TechnologyNotFoundException extends CapabilityException {
    public TechnologyNotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message, 404);
    }
}
