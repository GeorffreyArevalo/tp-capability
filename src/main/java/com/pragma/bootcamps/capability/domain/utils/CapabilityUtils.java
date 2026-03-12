package com.pragma.bootcamps.capability.domain.utils;

import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.models.CapabilityWithTechnologies;
import com.pragma.bootcamps.capability.domain.models.TechnologySummary;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CapabilityUtils {
    public static CapabilityWithTechnologies buildCapabilityWithTechnologies(Capability capability, List<TechnologySummary> technologies) {
        return CapabilityWithTechnologies.builder()
                .id(capability.getId())
                .name(capability.getName())
                .description(capability.getDescription())
                .technologies(technologies)
                .build();
    }
}
