package com.pragma.bootcamps.capability.infrastructure.adapters.restclients.webclient.adapters;

import com.pragma.bootcamps.capability.domain.clients.TechnologyAssociationClientPort;
import com.pragma.bootcamps.capability.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.capability.domain.exceptions.CapabilityTechnologiesCountException;
import com.pragma.bootcamps.capability.domain.exceptions.RepeatedTechnologiesException;
import com.pragma.bootcamps.capability.domain.exceptions.TechnologyMicroserviceException;
import com.pragma.bootcamps.capability.domain.exceptions.TechnologyNotFoundException;
import com.pragma.bootcamps.capability.domain.models.TechnologySummary;
import com.pragma.bootcamps.capability.infrastructure.adapters.restclients.webclient.dtos.requests.AssociationTechRequest;
import com.pragma.bootcamps.capability.infrastructure.adapters.restclients.webclient.dtos.responses.TechnologyListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyMicroserviceClientAdapter implements TechnologyAssociationClientPort {

    private static final String ASSOCIATE_TECHNOLOGIES_URL = "/tech/associate";
    private static final String GET_TECHNOLOGIES_URL = "/tech/capabilities/{capabilityId}/techs";

    @Value("${adapter.clients.clients.tech.base-url}")
    private String technologyMicroserviceBaseUrl;

    private final WebClient client;

    @Override
    public Mono<Void> associateTechnologies(Long capabilityId, List<Long> technologyIds) {
        return client.post()
                .uri(String.format("%s/%s", technologyMicroserviceBaseUrl, ASSOCIATE_TECHNOLOGIES_URL))
                .bodyValue(new AssociationTechRequest(capabilityId, technologyIds))
                .retrieve()
                .onStatus(status -> status.value() == 400, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new CapabilityTechnologiesCountException(body)))
                )
                .onStatus(status -> status.value() == 409, response ->
                        response.bodyToMono(String.class).flatMap(body -> Mono.error(new RepeatedTechnologiesException(body)))
                )
                .onStatus(status -> status.value() == 404, response ->
                        response.bodyToMono(String.class).flatMap(body -> Mono.error(new TechnologyNotFoundException(body)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException(body)))
                )
                .toBodilessEntity()
                .then();
    }

    @Override
    public Flux<TechnologySummary> getTechnologiesByCapabilityId(Long capabilityId) {
        return client.get()
                .uri(GET_TECHNOLOGIES_URL, capabilityId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new TechnologyMicroserviceException(
                                        ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.format(body)
                                ))
                        )
                )
                .bodyToMono(TechnologyListResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.data() != null ? response.data() : List.of()));
    }
}
