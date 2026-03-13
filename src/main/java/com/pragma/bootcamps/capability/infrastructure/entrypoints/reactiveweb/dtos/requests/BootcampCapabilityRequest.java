package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests;

import java.util.List;

public record BootcampCapabilityRequest(
        Long bootcampId,
        List<Long> capabilityIds
) {
}
