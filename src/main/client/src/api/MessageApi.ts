import {authenticatedFetch} from "./AuthenticatedFetch.ts";
import {Env} from "../Env.ts";

export interface Message {
    id?: string;
    incident: {
        id: string;
    };
    message: string;
    sender?: string;
    timestamp: string;
}

const createMessage = async (message: Message): Promise<Message> => {
    const response = await authenticatedFetch(`${Env.API_BASE_URL}/simulation/incident/message`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(message),
    });

    if (!response.ok) {
        console.error(response);
        throw new Error('Chyba při odesílání zprávy.');
    }
    return await response.json();
};

const getMessages = async (incidentId: string): Promise<Message[]> => {
    const response = await authenticatedFetch(`${Env.API_BASE_URL}/simulation/incident/${incidentId}/messages`);
    if (!response.ok) {
        console.error(response);
        throw new Error('Chyba při načítání zpráv.');
    }
    return await response.json();
};


export { createMessage, getMessages };