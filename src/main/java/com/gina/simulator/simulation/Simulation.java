package com.gina.simulator.simulation;

import com.gina.simulator.enums.Difficulty;
import com.gina.simulator.person.Person;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Person person;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int rating;

    private Difficulty difficulty;
}
