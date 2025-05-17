import {Button} from "react-bootstrap";
import SimulationResults from "../components/simulation-result/SimulationResult";
import "../index.css";
import { useEffect, useState } from "react";
import { getSimulationDetails } from "../api/SimulationApi";

interface IncidentResultsDTO {
    chosenCategory: string;
    correctCategory: string;
}

interface SimulationResultsDTO {
    username: string;
    result: number;
    difficulty: string;
    incidents: IncidentResultsDTO[];
}

interface SimulationResultsPageProps {
    simulationId: string;
    onBack: () => void;
}

function SimulationResultsPage({ simulationId, onBack }: SimulationResultsPageProps) {
    const [results, setResults] = useState<SimulationResultsDTO | null>(null);

    useEffect(() => {
        if (simulationId) {
            getSimulationDetails(simulationId)
                .then(data => setResults(data))
                .catch(err => console.error("Chyba při načítání výsledku", err));
        }
    }, [simulationId]);

    if (!results) return <div>Načítám výsledky...</div>;


    return (
        <div style={{ margin: "20px" }}>
            <SimulationResults results={results} />
            <Button variant="secondary" onClick={onBack}>
                Zpět
            </Button>
        </div>
    );
}


export default SimulationResultsPage;