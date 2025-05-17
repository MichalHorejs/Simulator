import { Env } from "../Env.ts";
import {LeaderboardResponse} from "../pages/LeaderboardsPage.tsx";

const getLeaderboards = async (
    difficulty: string,
    page: number = 0,
    limit: number = 10
): Promise<LeaderboardResponse> => {
    const response = await fetch(
        `${Env.API_BASE_URL}/leaderboards?difficulty=${difficulty}&page=${page}&limit=${limit}`
    );
    if (!response.ok) {
        throw new Error("Chyba při načítání leaderboardů");
    }
    return await response.json();
};

export { getLeaderboards };