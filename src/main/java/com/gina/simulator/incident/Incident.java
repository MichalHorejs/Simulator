package com.gina.simulator.incident;

import com.gina.simulator.enums.State;
import com.gina.simulator.simulation.Simulation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    Simulation simulation;

    private String phoneNumber;

    private State state;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
