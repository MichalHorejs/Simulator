import Button from "react-bootstrap/Button";
import "../index.css";
import "./css/ActiveSimulationPage.css";
import { finishSimulation } from "../api/SimulationApi.ts";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import Incidents, { Incident } from "../components/incidents/Incidents.tsx";
import IncidentDetail from "../components/incident-detail/IncidentDetail.tsx";

interface Person {
    id: string;
    username: string;
    role: string;
}

interface Simulation {
    id: string;
    person: Person;
    startTime: string;
    endTime?: string | null;
    rating: number;
    difficulty: string;
}

interface ActiveSimulationPageProps {
    onEndSimulation: () => void;
}

function ActiveSimulationPage({ onEndSimulation }: ActiveSimulationPageProps) {
    const navigate = useNavigate();
    const [simulation, setSimulation] = useState<Simulation | null>(null);
    const [selectedIncident, setSelectedIncident] = useState<Incident | null>(null);
    const [updatedIncident, setUpdatedIncident] = useState<Incident | undefined>(undefined);

    useEffect(() => {
        const sim = localStorage.getItem("simulation");
        if (!sim) {
            navigate("/inactive-simulation");
        } else {
            setSimulation(JSON.parse(sim));
        }
    }, [navigate]);

    const handleEndSimulation = async () => {
        if (!simulation) return;
        try {
            const result = await finishSimulation(simulation.id);
            console.log(result);
            localStorage.removeItem("simulation");
        } catch (error) {
            console.error(error);
        }
        onEndSimulation();
    };

    const handleCloseDetail = (updated?: Incident) => {
        if (updated) {
            setUpdatedIncident(updated);
        } else {
            setUpdatedIncident(undefined);
        }
        setSelectedIncident(null);
    };

    if (!simulation) return null;

    return (
        <div className="simulation-detail">
            <Incidents
                difficulty={simulation.difficulty}
                simulationId={simulation.id}
                onSelectIncident={setSelectedIncident}
                updatedIncident={updatedIncident}
                detailOpen={selectedIncident !== null}
            />
            {selectedIncident && (
                <IncidentDetail incident={selectedIncident} onClose={handleCloseDetail} />
            )}
            <Button
                variant="secondary"
                type="submit"
                onClick={handleEndSimulation}
                style={{ position: "fixed", top: "75px", right: "15px", zIndex: 1000 }}
            >
                Ukončit simulaci
            </Button>
        </div>
    );
}

export default ActiveSimulationPage;