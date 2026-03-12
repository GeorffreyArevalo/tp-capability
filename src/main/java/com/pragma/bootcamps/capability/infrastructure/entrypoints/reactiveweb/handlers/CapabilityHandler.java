package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.capability.domain.api.CapabilityServicePort;
import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests.CapabilityRequest;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.mappers.CapabilityRequestMapper;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.utils.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CapabilityHandler {

    private final CapabilityServicePort capabilityServicePort;
    private final CapabilityRequestMapper mapper;
    private final ValidatorUtil validatorUtil;


    public Mono<ServerResponse> listenSave(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CapabilityRequest.class)
                .doOnNext(capabilityRequest -> log.info("Received capability request: {}", capabilityRequest))
                .flatMap(validatorUtil::validate)
                .map(mapper::toDomain)
                .flatMap(capabilityServicePort::saveCapability)
                .map(mapper::toResponse)
                .flatMap(savedCapability -> ServerResponse.created(URI.create(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), savedCapability))
                );
    }

    public Mono<ServerResponse> listenListCapabilities(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortBy = request.queryParam("sortBy").orElse("name");
        String order = request.queryParam("order").orElse("asc");

        log.info("[HANDLER] listenListCapabilities called with page={}, size={}, sortBy={}, order={}", page, size, sortBy, order);
        return capabilityServicePort.getCapabilitiesWithTechnologies(page, size, sortBy, order)
                .map(mapper::toCapabilityWithTechnologiesResponse)
                .collectList()
                .doOnNext(dtoList -> log.info("[HANDLER] Capabilities mapped: {}", dtoList))
                .map(dtoList -> new PageImpl<>(dtoList, PageRequest.of(page, size), dtoList.size()))
                .flatMap(pageResult -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.OK.status(), pageResult))
                );
    }


}
