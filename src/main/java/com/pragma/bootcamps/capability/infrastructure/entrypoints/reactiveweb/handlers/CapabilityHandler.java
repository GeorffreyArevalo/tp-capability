package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.capability.domain.api.CapabilityServicePort;
import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests.CapabilityRequest;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.mappers.CapabilityRequestMapper;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.utils.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

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
                        .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), savedCapability))
                );
    }

}
