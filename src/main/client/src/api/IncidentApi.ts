import {Env} from "../Env.ts";
import {authenticatedFetch} from "./AuthenticatedFetch.ts";
import {FormData} from "../components/form/Form.tsx";

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

const saveIncident = async (
    incidentId: string,
    incidentData: FormData & { currentLocation: { lat: number; lon: number } }
) => {
    const payload = {
        category: incidentData.category,
        subcategory: incidentData.subcategory,
        specification: incidentData.specification,
        vehicleTypes: incidentData.cars,
        address: {
            district: incidentData.district,
            municipality: incidentData.municipality,
            latitude: incidentData.currentLocation.lat,
            longitude: incidentData.currentLocation.lon,
        }
    };
    const response = await authenticatedFetch(`${Env.API_BASE_URL}/simulation/incident/${incidentId}/save`, {
        method: 'POST',
        body: JSON.stringify(payload)
    });

    if (!response.ok) {
        throw new Error('Chyba při ukládání incidentu');
    }
    return await response.json();
}

export { createIncident, saveIncident };