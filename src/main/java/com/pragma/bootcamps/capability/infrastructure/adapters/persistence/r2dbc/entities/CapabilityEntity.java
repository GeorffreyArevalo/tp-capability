package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class CapabilityEntity {

    @Id
    private Long id;
    private String name;
    private String description;

}
