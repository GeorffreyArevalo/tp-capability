package com.pragma.bootcamps.capability.domain.spi;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampCapabilityPersistencePort {
    Mono<Void> saveAll(Long bootcampId, List<Long> capabilityIds);
    Flux<Long> findCapabilityIdsByBootcampId(Long bootcampId);
    Mono<Long> countBootcampsByCapability(Long capabilityId);
    Mono<Void> deleteAssociationsByBootcampId(Long bootcampId);
}
