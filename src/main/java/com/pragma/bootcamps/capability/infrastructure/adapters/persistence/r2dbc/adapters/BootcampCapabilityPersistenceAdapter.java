package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.adapters;

import com.pragma.bootcamps.capability.domain.spi.BootcampCapabilityPersistencePort;
import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.entities.BootcampCapabilityEntity;
import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.repositories.BootcampCapabilityReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootcampCapabilityPersistenceAdapter implements BootcampCapabilityPersistencePort  {

    private final BootcampCapabilityReactiveRepository bootcampCapabilityReactiveRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Void> saveAll(Long bootcampId, List<Long> capabilityIds) {
        return Flux.fromIterable(capabilityIds)
                .map(capId -> BootcampCapabilityEntity.builder()
                        .bootcampId(bootcampId)
                        .capabilityId(capId)
                        .build())
                .collectList()
                .flatMapMany(bootcampCapabilityReactiveRepository::saveAll)
                .then()
                .as(transactionalOperator::transactional);
    }

    @Override
    public Flux<Long> findCapabilityIdsByBootcampId(Long bootcampId) {
        return bootcampCapabilityReactiveRepository.findAllByBootcampId(bootcampId)
                .map(BootcampCapabilityEntity::getCapabilityId)
                .doOnNext(id -> log.info("[DB] findCapabilityIdsByBootcampId({}): capabilityId={}", bootcampId, id));
    }

    @Override
    public Mono<Long> countBootcampsByCapability(Long capabilityId) {
        return bootcampCapabilityReactiveRepository.countByCapabilityId(capabilityId)
                .doOnNext(count -> log.info("[DB] countBootcampsForCapability({}): count={}", capabilityId, count));
    }

    @Override
    public Mono<Void> deleteAssociationsByBootcampId(Long bootcampId) {
        return bootcampCapabilityReactiveRepository.deleteAllByBootcampId(bootcampId)
                .doOnSuccess(unused -> log.info("[DB] deleteAssociationsByBootcampId para bootcampId={}", bootcampId))
                .as(transactionalOperator::transactional);
    }

}
