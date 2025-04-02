import {Env} from "../Env.ts";
import {authenticatedFetch} from "./AuthenticatedFetch.ts";

const createIncident = async (simulationId: string) => {

    const response = await authenticatedFetch(`${Env.API_BASE_URL}/simulation/${simulationId}/incident`, {
        method: 'POST',
    })

    if (!response.ok) {
        console.error(response);
        throw new Error('Chyba při vytváření incidentu');
    }

    return await response.json();
}

export { createIncident };