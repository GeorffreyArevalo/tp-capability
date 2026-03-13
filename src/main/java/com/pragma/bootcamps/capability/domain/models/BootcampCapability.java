package com.pragma.bootcamps.capability.domain.models;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampCapability {
    private Long id;
    private Long bootcampId;
    private Long capabilityId;
}
