import React from 'react';
import './IncidentDetail.css'
import { Incident } from '../incidents/Incidents';
import Chat from "../chat/Chat.tsx";
import Form from '../form/Form.tsx';
import IncidentMap from "../incident-map/IncidentMap.tsx";

interface IncidentDetailProps {
    incident: Incident;
    onClose: (updated?: Incident) => void;
}

const IncidentDetail: React.FC<IncidentDetailProps> = ({ incident, onClose }) => {
    return (
        <div className="incident-detail">
            <div className="top-section">
                <div className="chat-container">
                    <Chat key={incident.id} incidentId={incident.id} />
                </div>
                <div className="form-container">
                    <Form incidentId={incident.id} onSaved={onClose} />
                </div>
            </div>
            <div className="map-container">
                <IncidentMap lat={49.54871346667767} lon={17.088198074587726} />
            </div>
        </div>
    );
};

export default IncidentDetail;