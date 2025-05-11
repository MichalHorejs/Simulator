import { MapContainer, TileLayer, Marker, useMap } from 'react-leaflet';
import { useEffect, useState } from 'react';
import L from 'leaflet';
import './IncidentMap.css'

interface IncidentMapProps {
    lat: number;
    lon: number;
    delayMs?: number;
}

const SetZoomOnMarker = ({ lat, lon, showMarker }: { lat: number; lon: number; showMarker: boolean }) => {
    const map = useMap();

    useEffect(() => {
        if (showMarker) {
            map.flyTo([lat, lon], 17); // přiblížení s animací
        }
    }, [showMarker, lat, lon, map]);

    return null;
};

const IncidentMap = ({ lat, lon, delayMs = 3000 }: IncidentMapProps) => {
    const [showMarker, setShowMarker] = useState(false);

    useEffect(() => {
        const timer = setTimeout(() => setShowMarker(true), delayMs);
        return () => clearTimeout(timer);
    }, [delayMs]);

    const markerIcon = L.icon({
        iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
    });

    return (
        <MapContainer center={[49.6, 17.25]} zoom={9} className="incident-map">
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> přispěvatelé'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {showMarker && (
                <Marker
                    position={[lat, lon]}
                    icon={markerIcon}
                    draggable
                />
            )}
            <SetZoomOnMarker lat={lat} lon={lon} showMarker={showMarker} />
        </MapContainer>
    );
};

export default IncidentMap;
