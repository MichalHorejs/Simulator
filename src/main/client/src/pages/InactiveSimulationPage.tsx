import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import '../index.css';
import { useState } from 'react';
import {startSimulation} from "../api/simulationApi.ts";

interface InactiveSimulationPageProps {
    user: string;
    onStartSimulation: () => void;
}

function InactiveSimulationPage({ user, onStartSimulation }: InactiveSimulationPageProps) {
    const [difficulty, setDifficulty] = useState<number>(1);
    const difficultyLabels = ['nejlehčí', 'lehká', 'střední', 'težká', 'nejtežší'];

    const handleStart = async () => {
        try {
            const simulation = await startSimulation(user, difficulty);
            localStorage.setItem("simulation", JSON.stringify(simulation));
            onStartSimulation();
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
            <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem' }}>
                <Form.Label style={{ marginRight: '1rem', whiteSpace: 'nowrap' }}>
                    Obtížnost: {difficultyLabels[difficulty - 1]}
                </Form.Label>
                <Form.Range
                    value={difficulty}
                    min={1}
                    max={5}
                    style={{ width: '300px' }}
                    onChange={(e) => setDifficulty(parseInt(e.target.value))}
                />
            </div>
            <Button variant="secondary" type="submit" onClick={handleStart} >
                Spustit simulaci
            </Button>
        </div>
    );
}

export default InactiveSimulationPage;