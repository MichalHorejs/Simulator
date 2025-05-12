package com.gina.simulator.incidentTemplate;

import com.gina.simulator.address.Address;
import com.gina.simulator.enums.Category;
import com.gina.simulator.enums.Subcategory;
import com.gina.simulator.enums.Urgency;
import com.gina.simulator.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class IncidentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @ManyToMany
    @JoinTable(name = "incident_template_vehicle",
            joinColumns = @JoinColumn(name = "incident_template_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private List<Vehicle> vehicles = new ArrayList<>();
}
