package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.capability.domain.api.BootcampCapabilityServicePort;
import com.pragma.bootcamps.capability.domain.enums.ExceptionStatusCode;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests.BootcampCapabilityRequest;
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

}
