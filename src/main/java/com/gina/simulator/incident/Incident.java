package com.gina.simulator.incident;

import com.gina.simulator.address.Address;
import com.gina.simulator.enums.*;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.simulation.Simulation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Simulation simulation;

    /**  Incident template chosen from data */
    @ManyToOne
    private IncidentTemplate incidentTemplate;

    private String phoneNumber;

    private State state;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**  Data chosen by user */
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

    private String context;

    @ElementCollection(targetClass = VehicleType.class)
    @CollectionTable(name = "incident_vehicle_type", joinColumns = @JoinColumn(name = "incident_id"))
    @Column(name = "vehicle_type")
    @Enumerated(EnumType.STRING)
    private Set<VehicleType> vehicleTypes;
}
