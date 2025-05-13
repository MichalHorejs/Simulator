import { MapContainer, TileLayer, Marker, useMap } from 'react-leaflet';
import { useEffect, useState } from 'react';
import L from 'leaflet';
import './IncidentMap.css'

interface IncidentMapProps {
    lat: number;
    lon: number;
    onLocationChange: (lat: number, lon: number) => void;
}

const SetZoomOnMarker = ({ lat, lon, showMarker, initialZoomDone, setInitialZoomDone }: {
    lat: number;
    lon: number;
    showMarker: boolean;
    initialZoomDone: boolean;
    setInitialZoomDone: (value: boolean) => void;
}) => {
    const map = useMap();

    useEffect(() => {
        if (showMarker && !initialZoomDone) {
            map.flyTo([lat, lon], 17);
            setInitialZoomDone(true);
        }
    }, [showMarker, lat, lon, map, initialZoomDone, setInitialZoomDone]);

    return null;
};

const IncidentMap = ({ lat, lon, onLocationChange }: IncidentMapProps) => {
    const [showMarker, setShowMarker] = useState(false);
    const [loadingText, setLoadingText] = useState("📡 Hledání polohy...");
    const [initialZoomDone, setInitialZoomDone] = useState(false);


    useEffect(() => {
        const delay = Math.floor(Math.random() * (15000 - 5000 + 1)) + 5000; // 5–15 s
        const timer = setTimeout(() => {
            setShowMarker(true);
            setLoadingText("");
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
                        eventHandlers={{
                            dragend: (e) => {
                                const marker = e.target;
                                const position = marker.getLatLng();
                                onLocationChange(position.lat, position.lng);
                            }
                        }}
                    />
                )}
                <SetZoomOnMarker
                    lat={lat}
                    lon={lon}
                    showMarker={showMarker}
                    initialZoomDone={initialZoomDone}
                    setInitialZoomDone={setInitialZoomDone}
                />
            </MapContainer>
        </div>
    );
};

export default IncidentMap;
