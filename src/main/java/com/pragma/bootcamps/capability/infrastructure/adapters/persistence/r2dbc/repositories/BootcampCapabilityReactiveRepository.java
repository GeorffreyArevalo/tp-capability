package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.repositories;

import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.entities.BootcampCapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BootcampCapabilityReactiveRepository extends ReactiveCrudRepository<BootcampCapabilityEntity, Long> {
    Flux<BootcampCapabilityEntity> findAllByBootcampId(Long capabilityId);
}
