package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.mappers;

import com.pragma.bootcamps.capability.domain.models.BootcampCapability;
import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.entities.BootcampCapabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface BootcampCapabilityEntityMapper {

    BootcampCapabilityEntity toEntity(BootcampCapability bootcampCapability);
    BootcampCapability toDomain(BootcampCapabilityEntity entity);

}
