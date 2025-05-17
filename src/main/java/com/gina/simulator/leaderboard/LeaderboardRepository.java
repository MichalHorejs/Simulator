package com.gina.simulator.leaderboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, UUID> {
    Leaderboard findLeaderboardBySimulationId(UUID simulationId);
    Optional<Leaderboard> findBySimulationId(UUID simulationId);
}
