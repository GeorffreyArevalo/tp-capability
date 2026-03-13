package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.mappers;

import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.models.CapabilityWithTechnologies;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests.CapabilityRequest;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.responses.CapabilityResponse;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.responses.CapabilitySummaryResponse;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.responses.CapabilityWithTechnologiesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface CapabilityRequestMapper {

    Capability toDomain(CapabilityRequest request);
    CapabilityResponse toResponse(Capability capability);
    CapabilityWithTechnologiesResponse toCapabilityWithTechnologiesResponse(CapabilityWithTechnologies model);
    CapabilitySummaryResponse toCapabilitySummaryResponse(Capability capability);
}
