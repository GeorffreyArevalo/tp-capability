package com.pragma.bootcamps.capability.domain.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampCapabilityServicePort {

    Mono<Void> associateCapabilities(Long bootcampId, List<Long> capabilityIds);

}
