package com.gina.simulator.leaderboard;

import com.gina.simulator.enums.Difficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/leaderboards")
@RequiredArgsConstructor
public class LeaderBoardApi {

    private final LeaderboardService leaderboardService;

    @GetMapping
    public Page<Leaderboard> getLeaderBoard(
            @RequestParam Difficulty difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return leaderboardService.getLeaderboards(difficulty, page, limit);
    }
}
