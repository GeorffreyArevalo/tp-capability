package com.pragma.bootcamps.capability.domain.exceptions;

import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;

public class CapabilityTechnologiesCountException extends CapabilityException{

    public CapabilityTechnologiesCountException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST, message, 400);
    }

}
