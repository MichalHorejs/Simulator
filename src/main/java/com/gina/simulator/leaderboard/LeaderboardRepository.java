package com.gina.simulator.leaderboard;

import com.gina.simulator.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, UUID> {
    Leaderboard findLeaderboardBySimulationId(UUID simulationId);
    Optional<Leaderboard> findBySimulationId(UUID simulationId);
    Page<Leaderboard> findByDifficulty(Difficulty difficulty, Pageable pageable);

}
