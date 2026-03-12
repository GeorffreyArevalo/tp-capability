package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record TechnologySummaryResponse(
        @Schema(description = "Unique identifier of the technology", example = "1")
        Long id,
        @Schema(description = "Name of the technology", example = "Java")
        String name
) {
}
