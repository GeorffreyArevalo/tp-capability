package com.pragma.bootcamps.capability.domain.api;

import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.models.CapabilityWithTechnologies;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapabilityServicePort {

    Mono<Capability> saveCapability(Capability capability);

    Flux<CapabilityWithTechnologies> getCapabilitiesWithTechnologies(int page, int size, String sortBy, String order);
}
