package com.pragma.bootcamps.capability.domain.exceptions;

import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;

public class InvalidCountException extends CapabilityException{
    public InvalidCountException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST, message, 400);
    }
}
