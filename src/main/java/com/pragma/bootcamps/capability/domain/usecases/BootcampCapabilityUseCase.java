package com.pragma.bootcamps.capability.domain.usecases;

import com.pragma.bootcamps.capability.domain.api.BootcampCapabilityServicePort;
import com.pragma.bootcamps.capability.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.capability.domain.exceptions.InvalidCountException;
import com.pragma.bootcamps.capability.domain.exceptions.NotFoundException;
import com.pragma.bootcamps.capability.domain.exceptions.RepeatedCapabilitiesException;
import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.spi.BootcampCapabilityPersistencePort;
import com.pragma.bootcamps.capability.domain.spi.CapabilityPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.pragma.bootcamps.capability.domain.constants.CapabilityConstants.MAX_CAPS;
import static com.pragma.bootcamps.capability.domain.constants.CapabilityConstants.MIN_CAPS;

@RequiredArgsConstructor
public class BootcampCapabilityUseCase implements BootcampCapabilityServicePort {

    private final BootcampCapabilityPersistencePort bootcampCapabilityPersistencePort;
    private final CapabilityPersistencePort capabilityPersistencePort;

    @Override
    public Mono<Void> associateCapabilities(Long bootcampId, List<Long> capabilityIds) {
        return Mono.just(capabilityIds)
                .filter(ids -> isValidCapabilitiesCount(ids, MIN_CAPS, MAX_CAPS))
                .switchIfEmpty(Mono.error(new InvalidCountException(ExceptionMessages.BOOTCAMP_CAPABILITIES_COUNT_INVALID.format(MIN_CAPS, MAX_CAPS))))
                .filter(this::hasNoRepeatedCapabilities)
                .switchIfEmpty(Mono.error(new RepeatedCapabilitiesException(ExceptionMessages.BOOTCAMP_CAPABILITIES_REPEATED.format())))
                .flatMap(ids -> capabilityPersistencePort.countByIds(ids)
                        .filter(count -> count == ids.size())
                        .switchIfEmpty(Mono.error(new NotFoundException(ExceptionMessages.CAPABILITY_NOT_FOUND.format())))
                        .thenReturn(ids))
                .flatMap(ids -> bootcampCapabilityPersistencePort.saveAll(bootcampId, ids))
                .then().log();
    }

    public Flux<Capability> getCapabilitiesByBootcampId(Long bootcampId) {
        return bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(bootcampId)
                .flatMap(capabilityPersistencePort::findCapabilityById);
    }

    private boolean isValidCapabilitiesCount(List<Long> capsIds, int min, int max) {
        return capsIds != null && capsIds.size() >= min && capsIds.size() <= max;
    }

    private boolean hasNoRepeatedCapabilities(List<Long> capsIds) {
        return capsIds != null && capsIds.stream().distinct().count() == capsIds.size();
    }
}
