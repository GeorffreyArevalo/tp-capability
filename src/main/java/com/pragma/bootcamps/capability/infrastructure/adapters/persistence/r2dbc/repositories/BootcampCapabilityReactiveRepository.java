package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.repositories;

import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.entities.BootcampCapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BootcampCapabilityReactiveRepository extends ReactiveCrudRepository<BootcampCapabilityEntity, Long> {


}
