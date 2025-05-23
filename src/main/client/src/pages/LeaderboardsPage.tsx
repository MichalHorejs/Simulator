import { useEffect, useState, useCallback } from "react";
import { Button, Table, ButtonGroup } from "react-bootstrap";
import { getLeaderboards, deleteLeaderboard } from "../api/LeaderboardsApi.ts";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/LoginContext.tsx";

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
    const { username } = useAuth();
    const [difficulty, setDifficulty] = useState<string>("EASIEST");
    const [leaderboards, setLeaderboards] = useState<LeaderboardItem[]>([]);
    const [page, setPage] = useState<number>(0);
    const [totalPages, setTotalPages] = useState<number>(1);
    const limit = 10;
    const navigate = useNavigate();

    const formatDate = (dateStr: string): string => {
        const date = new Date(dateStr);
        const day = ("0" + date.getDate()).slice(-2);
        const month = ("0" + (date.getMonth() + 1)).slice(-2);
        const year = date.getFullYear();
        const hours = ("0" + date.getHours()).slice(-2);
        const minutes = ("0" + date.getMinutes()).slice(-2);
        const seconds = ("0" + date.getSeconds()).slice(-2);
        return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
    };

    const fetchLeaderboards = useCallback(async () => {
        try {
            const data = await getLeaderboards(difficulty, page, limit);
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

    const handleDelete = async (leaderboardId: string) => {
        if (!window.confirm("Opravdu smazat leaderboard?")) return;
        try {
            await deleteLeaderboard(leaderboardId);
            await fetchLeaderboards();
        } catch (error) {
            console.error("Chyba při mazání leaderboardu", error);
        }
    };

    return (
        <div className="leaderboards-container" style={{ padding: "65px", maxWidth: "1280px", margin: "auto" }}>
            <h2>Žebříček</h2>
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
                    <th>Akce</th>
                </tr>
                </thead>
                <tbody>
                {leaderboards.map((item) => (
                    <tr key={item.id}>
                        <td>{item.username}</td>
                        <td>{item.score}</td>
                        <td>{formatDate(item.time)}</td>
                        <td>
                            <ButtonGroup>
                                <Button variant="secondary" onClick={() => handleDetail(item.simulationId)}>
                                    Detail
                                </Button>
                                {username === "admin" && (
                                    <Button variant="outline-danger" onClick={() => handleDelete(item.id)}>
                                        &#10006;
                                    </Button>
                                )}
                            </ButtonGroup>
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