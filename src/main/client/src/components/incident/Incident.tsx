import './Incident.css'
import incomingCall from "../../assets/incoming.png";
import processedCall from "../../assets/processed.png";
import finishedCall from "../../assets/finished.png";
import { Incident as IncidentType } from "../incidents/Incidents";

interface IncidentProps {
    incident: IncidentType;
    onSelect?: (incident: IncidentType) => void;
}

function Incident({ incident, onSelect }: IncidentProps) {
    const iconMap: { [key: string]: string } = {
        INCOMING: incomingCall,
        PROCESSED: processedCall,
        FINISHED: finishedCall
    };

    // Zakáže kliknutí, pokud incident není ve stavu INCOMING.
    const disabled = incident.state !== "INCOMING";

    const showIncident = () => {
        if (!disabled && onSelect) {
            onSelect(incident);
        }
    };

    return (
        <button className="incident" onClick={showIncident} disabled={disabled}>
            <span>{incident.phoneNumber}</span>
            <img src={iconMap[incident.state]} alt={incident.state} />
        </button>
    );
}

export default Incident;