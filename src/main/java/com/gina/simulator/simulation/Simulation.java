package com.gina.simulator.simulation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gina.simulator.enums.Difficulty;
import com.gina.simulator.incident.Incident;
import com.gina.simulator.person.Person;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnoreProperties({"password"}) // doesnt work ??
    @ManyToOne
    private Person person;

    @JsonIgnore
    @OneToMany(mappedBy = "simulation")
    private List<Incident> incidents;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int rating;

    private Difficulty difficulty;
}
