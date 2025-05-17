package com.gina.simulator.leaderboard;

import com.gina.simulator.enums.Difficulty;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;

    @Transactional
    public void save(String username, UUID simId, int score, Difficulty difficulty) {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setUsername(username);
        leaderboard.setSimulationId(simId);
        leaderboard.setScore(score);
        leaderboard.setDifficulty(difficulty);
        leaderboard.setTime(LocalDateTime.now());

        leaderboardRepository.save(leaderboard);
    }

    public Leaderboard getLeaderboard(UUID simId) {
        return leaderboardRepository.findBySimulationId(simId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid simulation id: " + simId));
    }

    public Page<Leaderboard> getLeaderboards(Difficulty difficulty, int page, int limit) {
        return leaderboardRepository.findByDifficulty(difficulty, PageRequest.of(page, limit));
    }
}
