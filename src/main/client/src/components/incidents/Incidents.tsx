import {useEffect, useState} from 'react';
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
        let active = true;
        let counter = 0;
        const maxCount = getMaxCount();

        const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

        const fetchNext = async () => {
            if (!active || counter >= maxCount) return;

            try {
                const newIncident = await createIncident(simulationId);
                if (!active) return;

                setIncidents(prev => [newIncident, ...prev]);
                counter++;

                await sleep(1000);
                await fetchNext();
            } catch (error) {
                console.error(error);
            }
        };

        fetchNext();
        return () => { active = false; };
    }, [difficulty, simulationId]);

    return (
        <div className="incidents">
            {incidents.map((incident) => (
                <Incident key={incident.id} incident={incident} onSelect={onSelectIncident} />
            ))}
        </div>
    );
}

export default Incidents;