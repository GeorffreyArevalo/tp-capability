package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.routes;


import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.docs.BootcampCapabilityOpenApi;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.handlers.BootcampCapabilityHandler;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.routes.paths.CapabilityPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class BootcampCapabilityRouter {

    private final CapabilityPath capabilityPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionCapabilityTechnology(BootcampCapabilityHandler handler) {
        return route()
                .POST(capabilityPath.getAssociateCapabilities(), handler::listenAssociateCapabilities, BootcampCapabilityOpenApi::associateCapabilities)
                .build();
    }

}
