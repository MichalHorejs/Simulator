package com.gina.simulator.incidentTemplate;

import com.gina.simulator.address.Address;
import com.gina.simulator.enums.Category;
import com.gina.simulator.enums.Subcategory;
import com.gina.simulator.enums.Urgency;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class IncidentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String specification;

    private Urgency urgency;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Subcategory subcategory;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude")),
            @AttributeOverride(name = "district", column = @Column(name = "district")),
            @AttributeOverride(name = "municipality", column = @Column(name = "municipality"))
    })
    private Address address;
}
