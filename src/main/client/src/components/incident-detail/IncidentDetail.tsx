import React, {useState} from 'react';
import './IncidentDetail.css'
import {Incident} from '../incidents/Incidents';
import Chat from "../chat/Chat.tsx";
import Form from '../form/Form.tsx';
import IncidentMap from "../incident-map/IncidentMap.tsx";

interface IncidentDetailProps {
    incident: Incident;
    onClose: (updated?: Incident) => void;
}

const IncidentDetail: React.FC<IncidentDetailProps> = ({incident, onClose}) => {
    const latitude = parseFloat(incident.incidentTemplate.address.latitude);
    const longitude = parseFloat(incident.incidentTemplate.address.longitude);
    const [currentLat, setCurrentLat] = useState(latitude);
    const [currentLon, setCurrentLon] = useState(longitude);

    return (
        <div className="incident-detail">
            <div className="top-section">
                <div className="chat-container">
                    <Chat key={incident.id} incidentId={incident.id}/>
                </div>
                <div className="form-container">
                    <Form incidentId={incident.id}
                          onSaved={onClose}
                          currentLocation={{lat: currentLat, lon: currentLon}}
                    />
                </div>
            </div>
            <div className="map-container">
                <IncidentMap lat={currentLat} lon={currentLon}
                             onLocationChange={(lat, lon) => {
                                 setCurrentLat(lat);
                                 setCurrentLon(lon);
                             }}/>
            </div>
        </div>
    );
};

export default IncidentDetail;