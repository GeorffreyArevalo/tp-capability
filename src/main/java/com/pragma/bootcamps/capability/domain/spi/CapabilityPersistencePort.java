package com.pragma.bootcamps.capability.domain.spi;

import com.pragma.bootcamps.capability.domain.models.Capability;
import reactor.core.publisher.Mono;

public interface CapabilityPersistencePort {

    Mono<Capability> saveCapability(Capability capability);
    Mono<Void> deleteCapability(Long capabilityId);
    Mono<Capability> findCapabilityByName(String name);

}
