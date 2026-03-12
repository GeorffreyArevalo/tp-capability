package com.pragma.bootcamps.capability.domain.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class CapabilityWithTechnologies {
    private Long id;
    private String name;
    private String description;
    private List<TechnologySummary> technologies;
}
