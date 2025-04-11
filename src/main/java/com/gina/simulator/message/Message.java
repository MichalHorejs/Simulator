package com.gina.simulator.message;

import com.gina.simulator.enums.From;
import com.gina.simulator.incident.Incident;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Incident incident;

    private String message;

    private From from;

    private LocalDateTime timestamp;
}
