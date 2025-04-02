import './Incident.css'
import incomingCall from "../../assets/incoming.png";
import processedCall from "../../assets/processed.png";
import finishedCall from "../../assets/finished.png";
import { Incident as IncidentType } from '../incidents/Incidents'; // Importujeme plný typ Incident


interface IncidentProps {
    incident: IncidentType;
    onSelect?: (incident: IncidentType) => void;
}

function Incident ({ incident, onSelect }: IncidentProps) {

    const iconMap: { [key: string]: string } = {
        INCOMING: incomingCall,
        PROCESSED: processedCall,
        FINISHED: finishedCall
    };

    const showIncident = () => {
        if (onSelect) {
            onSelect(incident);
        }
    };

    return (
        <button className="incident" onClick={showIncident}>
            <span>{incident.phoneNumber}</span>
            <img
                src={iconMap[incident.state]}
                alt={incident.state}
            />
        </button>
    )
}

export default Incident;