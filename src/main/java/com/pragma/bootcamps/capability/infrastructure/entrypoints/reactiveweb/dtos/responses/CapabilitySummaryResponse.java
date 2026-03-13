package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO representing a Capability summary")
public record CapabilitySummaryResponse(
        @Schema(description = "Unique identifier of the capability", example = "1")
        Long id,
        @Schema(description = "Name of the capability", example = "Java")
        String name
) {
}
