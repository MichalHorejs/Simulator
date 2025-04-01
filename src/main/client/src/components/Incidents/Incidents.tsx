import { useEffect, useState } from 'react';
import Incident from '../Incident/Incident';
import "./Incidents.css";

export interface Incident {
    id: string;
    title: string;
    description?: string;
    phoneNumber: string;
    state: "incoming" | "processed" | "finished";
}

interface IncidentsProps {
    difficulty: string;
}

function Incidents({ difficulty }: IncidentsProps) {
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

    const generateIncident = (index: number): Incident => {
        return {
            id: Math.random().toString(36).substring(2, 9),
            title: "Incident " + (index + 1),
            description: "Popis " + Math.floor(Math.random() * 100),
            phoneNumber: "+420 " + Math.floor(100000000 + Math.random() * 900000000),
            state: (["incoming", "processed", "finished"][
                Math.floor(Math.random() * 3)
            ] as "incoming" | "processed" | "finished")
        };
    };

    useEffect(() => {
        const maxCount = getMaxCount();
        const timer = setInterval(() => {
            setIncidents((prev) => {
                if (prev.length < maxCount) {
                    return [...prev, generateIncident(prev.length)];
                } else {
                    clearInterval(timer);
                    return prev;
                }
            });
        }, 1000);

        return () => clearInterval(timer);
    }, [difficulty]);

    return (
        <div className="incidents">
            {incidents.map((incident) => (
                <Incident key={incident.id} incident={incident} />
            ))}
        </div>
    );
}

export default Incidents;