package com.pragma.bootcamps.capability.domain.clients;

import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyAssociationClientPort {
    Mono<Void> associateTechnologies(Long capabilityId, List<Long> technologyIds);
}
