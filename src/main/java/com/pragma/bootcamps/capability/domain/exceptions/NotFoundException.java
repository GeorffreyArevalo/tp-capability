package com.pragma.bootcamps.capability.domain.exceptions;

import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;

public class NotFoundException extends CapabilityException{
    public NotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message, 404);
    }
}
