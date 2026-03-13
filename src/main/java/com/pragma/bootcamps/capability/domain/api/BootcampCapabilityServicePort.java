package com.pragma.bootcamps.capability.domain.api;

import com.pragma.bootcamps.capability.domain.models.Capability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampCapabilityServicePort {

    Mono<Void> associateCapabilities(Long bootcampId, List<Long> capabilityIds);
    Flux<Capability> getCapabilitiesByBootcampId(Long bootcampId);

}
