package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.adapters;

import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.spi.CapabilityPersistencePort;
import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.mappers.CapabilityEntityMapper;
import com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.repositories.CapabilityReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static reactor.netty.http.HttpConnectionLiveness.log;

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

    @Override
    public Flux<Capability> findCapabilitiesPagedAndSorted(int page, int size, String sortBy, String order) {
        Sort sort = Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return capabilityReactiveRepository.findAllBy(pageRequest)
                .map(mapper::toDomain)
                .doOnNext(cap -> log.info("[DB RESULT] capability_id={}, name={}, technology_count={}", cap.getId(), cap.getName(), cap.getTechnologyCount()));
    }

    @Override
    public Mono<Long> countByIds(List<Long> capabilityIds) {
        return capabilityReactiveRepository.countByIdIn(capabilityIds);
    }

    @Override
    public Mono<Capability> findCapabilityById(Long capabilityId) {
        return capabilityReactiveRepository.findById(capabilityId)
                .map(mapper::toDomain)
                .doOnNext(cap -> log.info("[DB RESULT] findCapabilityById({}): id={}, name={}, technology_count={}", capabilityId, cap.getId(), cap.getName(), cap.getTechnologyCount()));
    }

}
