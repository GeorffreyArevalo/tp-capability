package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CapabilityRequest(
        @NotBlank(message = "Name is required")
        @Schema(description = "Name of the capability", example = "Reactive Spring")
        String name,

        @NotBlank(message = "Description is required")
        @Schema(description = "Description of the capability", example = "A reactive framework for Spring applications")
        String description,

        @NotNull(message = "Technology IDs are required")
        @Schema(description = "List of technology IDs to associate", example = "[1,2,3]")
        List<Long> technologyIds
) {
}
