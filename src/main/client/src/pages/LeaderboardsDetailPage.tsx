// src/main/client/src/pages/LeaderboardsDetailPage.tsx
import { Button } from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import SimulationResults from "../components/simulation-result/SimulationResult";
import { getSimulationDetails } from "../api/SimulationApi";
import { SimulationResultsDTO } from "./SimulationResultsPage";
import {useEffect, useState} from "react";


function LeaderboardsDetailPage(){
    const { simId } = useParams<{ simId: string }>();
    const [results, setResults] = useState<SimulationResultsDTO | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (simId) {
            getSimulationDetails(simId)
                .then(data => setResults(data))
                .catch(err => console.error("Chyba při načítání výsledků", err));
        }
    }, [simId]);

    if (!results) return <div>Načítám výsledky...</div>;

    return (
        <div style={{ margin: "60px" }}>
            <SimulationResults results={results} />
            <Button variant="secondary" onClick={() => navigate("/leaderboards")}>
                Zpět
            </Button>
        </div>
    );
}


export default LeaderboardsDetailPage;