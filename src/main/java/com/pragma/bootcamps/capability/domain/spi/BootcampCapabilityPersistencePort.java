package com.pragma.bootcamps.capability.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampCapabilityPersistencePort {
    Mono<Void> saveAll(Long bootcampId, List<Long> capabilityIds);
}
