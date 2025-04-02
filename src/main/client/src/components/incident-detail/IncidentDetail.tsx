import React from 'react';
import { Incident } from '../incidents/Incidents'; // Upravte cestu dle umístění typu

interface IncidentDetailProps {
    incident: Incident;
}

const IncidentDetail: React.FC<IncidentDetailProps> = ({ incident }) => {
    return (
        <div className="incident-detail">
            <h2>Detail Incidentu</h2>
            <p>ID: {incident.id}</p>
            <p>Telefonní číslo: {incident.phoneNumber}</p>
            <p>Stav: {incident.state}</p>
            <p>Začátek: {incident.startTime}</p>
            <p>Konec: {incident.endTime}</p>
        </div>
    );
};

export default IncidentDetail;