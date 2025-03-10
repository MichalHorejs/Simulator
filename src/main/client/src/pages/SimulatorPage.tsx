import {useAuth} from "../context/LoginContext.tsx";
import SimulatorRedirect from "../components/SimulatorRedirect/SimulatorRedirect.tsx";

function SimulatorPage() {
    const { user } = useAuth()

    if (!user) {
        return <SimulatorRedirect />;
    }

    return (
        <div>
            <p>Simulatorpage</p><p>Simulatorpage</p>

        </div>
    )
}

export default SimulatorPage;