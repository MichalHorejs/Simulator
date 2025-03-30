import Button from "react-bootstrap/Button";
import '../index.css'

interface ActiveSimulationPageProps {
    onEndSimulation: () => void;
}

function ActiveSimulationPage({ onEndSimulation }: ActiveSimulationPageProps) {
    const handleEndSimulation = () => {
        localStorage.removeItem(`simulation`);
        onEndSimulation();
    };

    return (
        <div>
            <p>Simulace běží</p>
            <Button variant="secondary" type="submit" onClick={handleEndSimulation}>
                Ukončit simulaci
            </Button>
        </div>
    );
}

export default ActiveSimulationPage;