import {Env} from "../Env.ts";
import {authenticatedFetch} from "./AuthenticatedFetch.ts";

const startSimulation = async (username: string, difficulty: number) => {
    const payload = {
        difficulty,
        person: {username},
    }

    const response = await authenticatedFetch(`${Env.API_BASE_URL}/simulation/start`, {
        method: 'POST',
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        console.error(response);
        throw new Error('Chyba při spuštění simulace');
    }
    return await response.json();
}

const finishSimulation = async (id: string) => {
    const payload = {
        id,
    }

    const response = await authenticatedFetch(`${Env.API_BASE_URL}/simulation/finish`, {
        method: 'POST',
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        console.error(response);
        throw new Error('Chyba při zastavení simulace');
    }
    return await response.json();
}

const getSimulationDetails = async (simulationId: string) => {
    const response = await authenticatedFetch(`${Env.API_BASE_URL}/simulation/${simulationId}/detail`);
    if (!response.ok) {
        console.error(response);
        throw new Error("Chyba při získávání výsledků simulace");
    }
    return await response.json();
};

export { startSimulation, finishSimulation, getSimulationDetails };