package com.pragma.bootcamps.capability.domain.exceptions;

import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;

public class CapabilityAlreadyExistsException extends CapabilityException{

    public CapabilityAlreadyExistsException(String message) {
        super(ExceptionStatusCode.CONFLICT, message, 409);
    }

}
