package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.mappers;

import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.entities.CapabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CapabilityEntityMapper {

    Capability toDomain(CapabilityEntity entity);
    CapabilityEntity toEntity(Capability capability);

}
