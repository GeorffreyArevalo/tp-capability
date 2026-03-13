package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.routes.paths;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "routes.paths")
public class CapabilityPath {

    private final String capabilities;
    private final String capabilitiesList;
    private String associateCapabilities;


}
