import {useAuth} from "../context/LoginContext.tsx";
import SimulatorRedirect from "../components/SimulatorRedirect/SimulatorRedirect.tsx";
import InactiveSimulationPage from "./InactiveSimulationPage.tsx";
import {useState} from "react";
import ActiveSimulationPage from "./ActiveSimulationPage.tsx";

function SimulatorPage() {
    const { username } = useAuth()
    const [simulationActive, setSimulationActive] = useState<boolean>(false);

    if (!username) {
        return <SimulatorRedirect />;
    }

    return (
        <div>
            {simulationActive ? (
                <ActiveSimulationPage onEndSimulation={() => setSimulationActive(false)} />
            ) : (
                <InactiveSimulationPage user={username} onStartSimulation={() => setSimulationActive(true)} />
            )}
        </div>
    )
}

export default SimulatorPage;