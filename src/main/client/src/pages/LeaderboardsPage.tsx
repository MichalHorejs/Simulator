import { useEffect, useState, useCallback } from "react";
import { Button, Table, ButtonGroup } from "react-bootstrap";
import { getLeaderboards } from "../api/LeaderboardsApi.ts";
import { useNavigate} from "react-router-dom";

export interface LeaderboardItem {
    id: string;
    username: string;
    simulationId: string;
    score: number;
    time: string;
    difficulty: string;
}

export interface LeaderboardResponse {
    content: LeaderboardItem[];
    totalPages: number;
    number: number;
}

const difficultyOrder: string[] = ["EASIEST", "EASY", "MEDIUM", "HARD", "HARDEST"];
const difficultyLabels: Record<string, string> = {
    "EASIEST": "nejlehčí",
    "EASY": "lehká",
    "MEDIUM": "střední",
    "HARD": "težká",
    "HARDEST": "nejtěžší"
};

function LeaderboardsPage() {
    const [difficulty, setDifficulty] = useState<string>("EASIEST");
    const [leaderboards, setLeaderboards] = useState<LeaderboardItem[]>([]);
    const [page, setPage] = useState<number>(0);
    const [totalPages, setTotalPages] = useState<number>(1);
    const limit = 10;
    const navigate = useNavigate();

    const fetchLeaderboards = useCallback(async () => {
        try {
            const data = await getLeaderboards(difficulty, page, limit);
            console.log("Načtená data:", data);
            setLeaderboards(data.content);
            setTotalPages(data.totalPages);
        } catch (error) {
            console.error("Chyba při načítání leaderboardů", error);
        }
    }, [difficulty, page]);

    useEffect(() => {
        void fetchLeaderboards();
    }, [fetchLeaderboards]);

    const handleDifficultyChange = (diff: string) => {
        setDifficulty(diff);
        setPage(0);
    };

    const handleDetail = (simulationId: string) => {
        navigate(`/leaderboards/simulation/${simulationId}`);
    };

    return (
        <div className="leaderboards-container" style={{ padding: "60px", maxWidth: "1280px", margin: "auto" }}>
            <h2>Leaderboards</h2>
            <div style={{ marginBottom: "20px" }}>
                <ButtonGroup>
                    {difficultyOrder.map((diff) => (
                        <Button
                            key={diff}
                            variant={diff === difficulty ? "secondary" : "outline-secondary"}
                            onClick={() => handleDifficultyChange(diff)}
                        >
                            {difficultyLabels[diff]}
                        </Button>
                    ))}
                </ButtonGroup>
            </div>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Uživatel</th>
                    <th>Skóre</th>
                    <th>Datum</th>
                    <th>Detail</th>
                </tr>
                </thead>
                <tbody>
                {leaderboards.map((item) => (
                    <tr key={item.id}>
                        <td>{item.username}</td>
                        <td>{item.score}</td>
                        <td>{item.time}</td>
                        <td>
                            <Button variant="secondary" onClick={() => handleDetail(item.simulationId)}>
                                Detail
                            </Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
            <div style={{ marginTop: "20px" }}>
                <Button variant="secondary" disabled={page === 0} onClick={() => setPage(page - 1)} style={{ marginRight: "10px" }}>
                    Předchozí
                </Button>
                <span>
                    Stránka {page + 1} z {totalPages}
                </span>
                <Button variant="secondary" disabled={page + 1 >= totalPages} onClick={() => setPage(page + 1)} style={{ marginLeft: "10px" }}>
                    Další
                </Button>
            </div>
        </div>
    );
}

export default LeaderboardsPage;