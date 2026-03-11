package com.pragma.bootcamps.capability.domain.api;

import com.pragma.bootcamps.capability.domain.models.Capability;
import reactor.core.publisher.Mono;

public interface CapabilityServicePort {

    Mono<Capability> saveCapability(Capability capability);

}
