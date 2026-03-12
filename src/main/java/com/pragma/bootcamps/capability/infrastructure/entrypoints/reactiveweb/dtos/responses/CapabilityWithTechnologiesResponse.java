package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CapabilityWithTechnologiesResponse(
        @Schema(description = "Unique identifier of the capability", example = "1")
        Long id,
        @Schema(description = "Name of the capability", example = "Reactive Spring")
        String name,
        @Schema(description = "Description of the capability", example = "A reactive framework for Spring applications")
        String description,
        @Schema(description = "List of associated technologies (id and name only)")
        List<TechnologySummaryResponse> technologies
){
}
