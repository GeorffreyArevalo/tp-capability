package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.routes;

import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.docs.CapabilityOpenApi;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.handlers.CapabilityHandler;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.routes.paths.CapabilityPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class CapabilityRouter {

    private final CapabilityPath paths;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(CapabilityHandler handler) {
        return route()
                .POST(paths.getCapabilities(), handler::listenSave, CapabilityOpenApi::saveCapability)
                .GET(paths.getCapabilitiesList(), handler::listenListCapabilities, CapabilityOpenApi::listCapabilities)
                .build();
    }

}
