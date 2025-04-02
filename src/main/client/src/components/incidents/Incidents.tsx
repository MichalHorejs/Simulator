import { useEffect, useState } from 'react';
import Incident from '../incident/Incident';
import "./Incidents.css";
import {createIncident} from "../../api/IncidentApi.ts";

export interface Incident {
    id: string;
    simulationId: string;
    phoneNumber: string;
    state: "INCOMING" | "PROCESSED" | "FINISHED";
    startTime: string;
    endTime?: string | null;
}

interface IncidentsProps {
    simulationId: string;
    difficulty: string;
    onSelectIncident?: (incident: Incident) => void;
}

function Incidents({ simulationId, difficulty, onSelectIncident }: IncidentsProps) {
    const [incidents, setIncidents] = useState<Incident[]>([]);

    const getMaxCount = (): number => {
        let count = 1;
        if (difficulty === "EASIEST") count = 2;
        else if (difficulty === "EASY") count = 3;
        else if (difficulty === "MEDIUM") count = 5;
        else if (difficulty === "HARD") count = 7;
        else if (difficulty === "HARDEST") count = 9;
        return count;
    };

    useEffect(() => {
        const maxCount = getMaxCount();
        const timer = setInterval(async () => {
            setIncidents((prev) => {
                if (prev.length >= maxCount) {
                    clearInterval(timer);
                }
                return prev;
            });
            if (incidents.length < maxCount) {
                try {
                    const newIncident = await createIncident(simulationId);
                    setIncidents((prev) => {
                        if (prev.length < maxCount) {
                            return [newIncident, ...prev];
                        }
                        return prev;
                    });
                } catch (error) {
                    console.error(error);
                }
            }
        }, 1000);
        return () => clearInterval(timer);
    }, [difficulty, simulationId, incidents.length]);

    return (
        <div className="incidents">
            {incidents.map((incident) => (
                <Incident key={incident.id} incident={incident} onSelect={onSelectIncident} />
            ))}
        </div>
    );
}

export default Incidents;