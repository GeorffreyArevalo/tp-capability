package com.pragma.bootcamps.capability.infrastructure.adapters.restclients.webclient.dtos.responses;

import com.pragma.bootcamps.capability.domain.models.TechnologySummary;

import java.util.List;

public record TechnologyListResponse(
        List<TechnologySummary> data
) {
}
