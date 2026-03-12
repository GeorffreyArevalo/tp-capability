package com.pragma.bootcamps.capability.infrastructure.adapters.restclients.webclient.dtos.requests;

import java.util.List;

public record AssociationTechRequest(
        Long capabilityId, List<Long> technologyIds
) {
}
