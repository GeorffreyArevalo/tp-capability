package com.pragma.bootcamps.capability.infrastructure.adapters.persistence.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_capability")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BootcampCapabilityEntity {

    @Id
    @Column("bootcamp_capability_id")
    private Long id;
    @Column("bootcamp_id")
    private Long bootcampId;
    @Column("capability_id")
    private Long capabilityId;

}
