import React from 'react';
import './IncidentDetail.css'
import { Incident } from '../incidents/Incidents';
import Chat from "../chat/Chat.tsx"; // Upravte cestu dle umístění typu

interface IncidentDetailProps {
    incident: Incident;
}

const IncidentDetail: React.FC<IncidentDetailProps> = ({ incident }) => {
    return (
        <div className="incident-detail">
            <div className="top-section">
                <div className="chat-container">
                    <Chat key={incident.id} incidentId={incident.id} />
                </div>
                <div className="form-container">
                    <h3>Formulář</h3>
                </div>
            </div>
            <div className="map-container">
                <h3>Mapa</h3>
                <h4>Detail Incidentu</h4>
                <p>ID: {incident.id}</p>
                <p>Telefonní číslo: {incident.phoneNumber}</p>
                <p>Stav: {incident.state}</p>
                <p>Začátek: {incident.startTime}</p>
                <p>Konec: {incident.endTime}</p>
            </div>
        </div>
    );
};

export default IncidentDetail;