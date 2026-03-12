package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.repositories;

import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.entities.CapabilityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapabilityReactiveRepository extends ReactiveCrudRepository<CapabilityEntity, Long> {
    Mono<CapabilityEntity> findByName(String name);
    Flux<CapabilityEntity> findAllBy(Pageable pageable);
}
