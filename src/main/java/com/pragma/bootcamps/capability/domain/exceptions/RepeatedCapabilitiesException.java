package com.pragma.bootcamps.capability.domain.exceptions;

import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;

public class RepeatedCapabilitiesException extends CapabilityException{
    public RepeatedCapabilitiesException(String message) {
        super(ExceptionStatusCode.CONFLICT, message, 409);
    }
}
