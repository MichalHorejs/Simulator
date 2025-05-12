package com.gina.simulator.vehicle;

import com.gina.simulator.enums.VehicleType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Vehicle {

    @Id
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private VehicleType type;
}
