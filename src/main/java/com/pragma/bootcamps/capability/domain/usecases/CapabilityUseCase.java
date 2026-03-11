package com.pragma.bootcamps.capability.domain.usecases;

import com.pragma.bootcamps.capability.domain.api.CapabilityServicePort;
import com.pragma.bootcamps.capability.domain.clients.TechnologyAssociationClientPort;
import com.pragma.bootcamps.capability.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.capability.domain.exceptions.CapabilityAlreadyExistsException;
import com.pragma.bootcamps.capability.domain.exceptions.CapabilityTechnologiesCountException;
import com.pragma.bootcamps.capability.domain.exceptions.SagaCompensationException;
import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.spi.CapabilityPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.pragma.bootcamps.capability.domain.constants.CapabilityConstants.MAX_TECHS;
import static com.pragma.bootcamps.capability.domain.constants.CapabilityConstants.MIN_TECHS;

@RequiredArgsConstructor
public class CapabilityUseCase implements CapabilityServicePort {

    private final CapabilityPersistencePort capabilityPersistencePort;
    private final TechnologyAssociationClientPort technologyAssociationClientPort;


    public Mono<Capability> saveCapability(Capability capability) {
        var techIds = capability.getTechnologyIds();
        return Mono.just(capability)
                .filter(cap -> isValidTechnologiesCount(techIds, MIN_TECHS, MAX_TECHS))
                .switchIfEmpty(Mono.error(new CapabilityTechnologiesCountException(
                        ExceptionMessages.CAPABILITY_TECHNOLOGIES_COUNT_INVALID.format()
                )))
                .filter(cap -> hasNoRepeatedTechnologies(techIds))
                .switchIfEmpty(Mono.error(new CapabilityTechnologiesCountException(
                        ExceptionMessages.CAPABILITY_TECHNOLOGIES_REPEATED.format()
                )))
                .flatMap(this::validateUniqueName)
                .flatMap(this::saveAndAssociateTechnologies);
    }

    private Mono<Capability> validateUniqueName(Capability capability) {
        return capabilityPersistencePort.findCapabilityByName(capability.getName())
                .flatMap(existing -> Mono.<Capability>error(new CapabilityAlreadyExistsException(
                        ExceptionMessages.CAPABILITY_ALREADY_EXISTS.format(capability.getName())
                )))
                .switchIfEmpty(Mono.just(capability));
    }

    private Mono<Capability> saveAndAssociateTechnologies(Capability capability) {
        var techIds = capability.getTechnologyIds();
        return capabilityPersistencePort.saveCapability(capability)
                .flatMap(savedCap -> technologyAssociationClientPort
                        .associateTechnologies(savedCap.getId(), techIds)
                        .thenReturn(savedCap)
                        .onErrorResume(e -> capabilityPersistencePort.deleteCapability(savedCap.getId())
                                .then(Mono.error(new SagaCompensationException(
                                        ExceptionMessages.SAGA_COMPENSATION_ASSOCIATION_FAILURE.getMessage()
                                )))
                        )
                );
    }

    public boolean isValidTechnologiesCount(List<Long> techIds, int min, int max) {
        return techIds != null && techIds.size() >= min && techIds.size() <= max;
    }

    public boolean hasNoRepeatedTechnologies(List<Long> techIds) {
        return techIds != null && techIds.stream().distinct().count() == techIds.size();
    }
}
