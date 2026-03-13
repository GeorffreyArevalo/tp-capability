package com.pragma.bootcamps.capability.domain.spi;

import com.pragma.bootcamps.capability.domain.models.Capability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityPersistencePort {

    Mono<Capability> saveCapability(Capability capability);
    Mono<Void> deleteCapability(Long capabilityId);
    Mono<Capability> findCapabilityByName(String name);
    Flux<Capability> findCapabilitiesPagedAndSorted(int page, int size, String sortBy, String order);
    Mono<Long> countByIds(List<Long> capabilityIds);
    Mono<Capability> findCapabilityById(Long capabilityId);
}
