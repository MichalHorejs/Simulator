import Button from "react-bootstrap/Button";
import '../index.css'
import {finishSimulation} from "../api/SimulationApi.ts";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";

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
    user: string;
    onEndSimulation: () => void;
}

function ActiveSimulationPage({ onEndSimulation }: ActiveSimulationPageProps) {

    const navigate = useNavigate();
    const [simulation, setSimulation] = useState<Simulation | null>(null);

    useEffect(() => {
        const simulation = localStorage.getItem("simulation");
        if (!simulation) {
            navigate("/inactive-simulation");
        } else {
            setSimulation(JSON.parse(simulation));
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

    if (!simulation) {
        return null;
    }

    return (
        <div>
            <p>Simulace běží</p>
            <Button variant="secondary" type="submit" onClick={handleEndSimulation}>
                Ukončit simulaci
            </Button>
            <pre>{JSON.stringify(simulation, null, 2)}</pre>
        </div>
    );
}

export default ActiveSimulationPage;