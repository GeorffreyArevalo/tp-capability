package com.pragma.bootcamps.capability.domain.clients;

import com.pragma.bootcamps.capability.domain.models.TechnologySummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyAssociationClientPort {
    Mono<Void> associateTechnologies(Long capabilityId, List<Long> technologyIds);
    Flux<TechnologySummary> getTechnologiesByCapabilityId(Long capabilityId);
}
