import {useAuth} from "../context/LoginContext.tsx";
import SimulatorRedirect from "../components/simulator-redirect/SimulatorRedirect.tsx";
import InactiveSimulationPage from "./InactiveSimulationPage.tsx";
import {useState} from "react";
import ActiveSimulationPage from "./ActiveSimulationPage.tsx";
import SimulationResultsPage from "./SimulationResultsPage.tsx";

type SimulationState = "inactive" | "active" | "results";


function SimulatorPage() {
    const { username } = useAuth()
    const [simulationState, setSimulationState] = useState<SimulationState>("inactive");
    const [simulationFinishedId, setSimulationFinishedId] = useState<string | null>(null);


    if (!username) {
        return <SimulatorRedirect />;
    }

    switch (simulationState) {
        case "active":
            return (
                <ActiveSimulationPage
                    onEndSimulation={(simId: string) => {
                        setSimulationFinishedId(simId);
                        setSimulationState("results");
                    }}
                />
            );
        case "results":
            return simulationFinishedId ? (
                <SimulationResultsPage
                    simulationId={simulationFinishedId}
                    onBack={() => {
                        setSimulationState("inactive");
                        setSimulationFinishedId(null);
                    }}
                />
            ) : (
                <div>Chyba při načítání výsledků</div>
            );
        default:
            return (
                <InactiveSimulationPage
                    user={username}
                    onStartSimulation={() => setSimulationState("active")}
                />
            );
    }
}

export default SimulatorPage;