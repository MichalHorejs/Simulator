import './Incident.css'
import incomingCall from "../../assets/incoming.png";
import processedCall from "../../assets/processed.png";
import finishedCall from "../../assets/finished.png";

interface IncidentProps {
    incident: {
        phoneNumber: string;
        state: 'incoming' | 'processed' | 'finished';
    };
}

function Incident ({ incident }: IncidentProps) {

    const iconMap: { [key: string]: string } = {
        incoming: incomingCall,
        processed: processedCall,
        finished: finishedCall
    };

    const handleClick = () => {
        alert('Telefon: ' + incident.phoneNumber);
    };

    return (
        <button className="incident" onClick={handleClick}>
            <span>{incident.phoneNumber}</span>
            <img
                src={iconMap[incident.state]}
                alt={incident.state}
                className="incident-icon"
            />
        </button>
    )
}

export default Incident;