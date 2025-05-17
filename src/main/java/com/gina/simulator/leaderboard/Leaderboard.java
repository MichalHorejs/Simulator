package com.gina.simulator.leaderboard;

import com.gina.simulator.enums.Difficulty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private UUID simulationId;

    private int score;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
}
