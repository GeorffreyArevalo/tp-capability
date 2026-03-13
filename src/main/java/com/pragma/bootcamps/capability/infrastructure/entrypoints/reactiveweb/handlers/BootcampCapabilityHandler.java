package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.capability.domain.api.BootcampCapabilityServicePort;
import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests.BootcampCapabilityRequest;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.mappers.CapabilityRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.URI;

import static com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootcampCapabilityHandler {

    private final BootcampCapabilityServicePort bootcampCapabilityServicePort;
    private final CapabilityRequestMapper mapper;

    public Mono<ServerResponse> listenAssociateCapabilities(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BootcampCapabilityRequest.class)
                .doOnNext(request -> log.info("Received bootcamp-capability association request: {}", request))
                .flatMap(request ->
                        bootcampCapabilityServicePort.associateCapabilities(request.bootcampId(), request.capabilityIds())
                )
                .then(ServerResponse.created(URI.create(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), null))
                );
    }

    public Mono<ServerResponse> listenGetCapabilitiesByBootcampId(ServerRequest request) {
        Long bootcampId = Long.valueOf(request.pathVariable("bootcampId"));
        return bootcampCapabilityServicePort.getCapabilitiesByBootcampId(bootcampId)
                .map(mapper::toCapabilitySummaryResponse)
                .collectList()
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.OK.status(), list)));
    }

}
