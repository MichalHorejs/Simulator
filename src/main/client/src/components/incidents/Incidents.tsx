import { useEffect, useState, useCallback } from "react";
import Incident from "../incident/Incident";
import "./Incidents.css";
import { createIncident, incidentPickedUp } from "../../api/IncidentApi.ts";

export interface IncidentTemplate {
    address: {
        latitude: string;
        longitude: string;
    };
}

export interface Incident {
    id: string;
    simulationId: string;
    phoneNumber: string;
    state: "INCOMING" | "PROCESSED" | "FINISHED";
    startTime: string;
    endTime?: string | null;
    incidentTemplate: IncidentTemplate;
}

interface IncidentsProps {
    simulationId: string;
    difficulty: string;
    onSelectIncident?: (incident: Incident) => void;
    updatedIncident?: Incident;
    detailOpen: boolean;
}

function Incidents({ simulationId, difficulty, onSelectIncident, updatedIncident, detailOpen }: IncidentsProps) {
    const [incidents, setIncidents] = useState<Incident[]>([]);

    const getRandomDelay = (difficulty: string): number => {
        let minDelay: number, maxDelay: number;
        switch (difficulty) {
            case "EASIEST":
                minDelay = 2 * 60 * 1000;
                maxDelay = 3 * 60 * 1000;
                break;
            case "EASY":
                minDelay = 1.5 * 60 * 1000;
                maxDelay = 2.5 * 60 * 1000;
                break;
            case "MEDIUM":
                minDelay = 1.25 * 60 * 1000;
                maxDelay = 1.45 * 60 * 1000;
                break;
            case "HARD":
                minDelay = 0.75 * 60 * 1000;
                maxDelay = 1.25 * 60 * 1000;
                break;
            case "HARDEST":
                minDelay = 0.25 * 60 * 1000;
                maxDelay = 0.75 * 60 * 1000;
                break;
            default:
                minDelay = 1000;
                maxDelay = 1000;
        }
        return Math.floor(Math.random() * (maxDelay - minDelay + 1)) + minDelay;
    };

    const getMaxCount = useCallback((): number => {
        let count = 1;
        if (difficulty === "EASIEST") count = 2;
        else if (difficulty === "EASY") count = 3;
        else if (difficulty === "MEDIUM") count = 5;
        else if (difficulty === "HARD") count = 7;
        else if (difficulty === "HARDEST") count = 9;
        return count;
    }, [difficulty]);

    useEffect(() => {
        let active = true;
        let counter = 0;
        const maxCount = getMaxCount();
        // const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

        const fetchNext = async () => {
            if (!active || counter >= maxCount) return;
            try {
                const newIncident = await createIncident(simulationId);
                if (!active) return;
                setIncidents(prev => [newIncident, ...prev]);
                counter++;
                // await sleep(1000);
                const delay = getRandomDelay(difficulty);
                await new Promise(resolve => setTimeout(resolve, delay));
                await fetchNext();
            } catch (error) {
                console.error(error);
            }
        };

        void fetchNext();
        return () => { active = false; };
    }, [difficulty, simulationId, getMaxCount]);

    useEffect(() => {
        if (updatedIncident) {
            setIncidents(prev =>
                prev.map(inc => (inc.id === updatedIncident.id ? updatedIncident : inc))
            );
        }
    }, [updatedIncident]);

    const handleSelectIncident = (incident: Incident) => {
        if (incident.state === "INCOMING") {

            void incidentPickedUp(incident.id);
            const updatedIncident: Incident = { ...incident, state: "PROCESSED" };
            setIncidents(prev =>
                prev.map(inc => (inc.id === updatedIncident.id ? updatedIncident : inc))
            );
            if (onSelectIncident) {
                onSelectIncident(updatedIncident);
            }
        }
    };

    return (
        <div className="incidents" style={{ pointerEvents: detailOpen ? "none" : "auto" }}>
            {incidents.map(incident => (
                <Incident key={incident.id} incident={incident} onSelect={handleSelectIncident} />
            ))}
        </div>
    );
}

export default Incidents;