import { MapContainer, TileLayer, Marker, useMap } from 'react-leaflet';
import { useEffect, useState } from 'react';
import L from 'leaflet';
import './IncidentMap.css'

interface IncidentMapProps {
    lat: number;
    lon: number;
}

const SetZoomOnMarker = ({ lat, lon, showMarker }: { lat: number; lon: number; showMarker: boolean }) => {
    const map = useMap();

    useEffect(() => {
        if (showMarker) {
            map.flyTo([lat, lon], 17);
        }
    }, [showMarker, lat, lon, map]);

    return null;
};

const IncidentMap = ({ lat, lon }: IncidentMapProps) => {
    const [showMarker, setShowMarker] = useState(false);
    const [loadingText, setLoadingText] = useState("📡 Hledání polohy...");

    useEffect(() => {
        const delay = Math.floor(Math.random() * (15000 - 5000 + 1)) + 5000; // 5–15 s
        const timer = setTimeout(() => {
            setShowMarker(true);
            setLoadingText(""); // skryj text
        }, delay);

        return () => clearTimeout(timer);
    }, []);

    const markerIcon = L.icon({
        iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
    });

    return (
        <div style={{ position: 'relative', height: '100%', width: '100%' }}>
            {!showMarker && (
                <div className="gps-loading">
                    {loadingText}
                </div>
            )}
            <MapContainer center={[49.6, 17.25]} zoom={9} style={{ height: '100%', width: '100%' }}>
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
        </div>
    );
};

export default IncidentMap;
