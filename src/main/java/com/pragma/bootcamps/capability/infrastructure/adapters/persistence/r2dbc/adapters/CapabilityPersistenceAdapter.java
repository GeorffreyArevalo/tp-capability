package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.adapters;

import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.spi.CapabilityPersistencePort;
import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.mappers.CapabilityEntityMapper;
import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.repositories.CapabilityReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CapabilityPersistenceAdapter implements CapabilityPersistencePort {

    private final CapabilityReactiveRepository capabilityReactiveRepository;
    private final CapabilityEntityMapper mapper;

    @Override
    public Mono<Capability> saveCapability(Capability capability) {
        return capabilityReactiveRepository.save( mapper.toEntity(capability) )
                .map( mapper::toDomain );
    }

    @Override
    public Mono<Void> deleteCapability(Long capabilityId) {
        return capabilityReactiveRepository.deleteById(capabilityId);
    }

    @Override
    public Mono<Capability> findCapabilityByName(String name) {
        return capabilityReactiveRepository.findByName(name)
                .map( mapper::toDomain );
    }

}
