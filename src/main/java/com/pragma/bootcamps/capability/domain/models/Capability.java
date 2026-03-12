package com.pragma.bootcamps.capability.domain.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Capability {

    private Long id;
    private String name;
    private String description;
    private List<Long> technologyIds;
    private Integer technologyCount;

}
