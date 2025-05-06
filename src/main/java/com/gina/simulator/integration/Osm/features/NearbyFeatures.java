package com.gina.simulator.integration.Osm.features;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
public class NearbyFeatures {
    private Map<String, Node> nodeMap = new HashMap<>();
    private List<Building> buildings = new ArrayList<>();
    private List<Natural> naturals = new ArrayList<>();
    private List<Landuse> landuses = new ArrayList<>();
    private List<Highway> highways = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("Tvé okolí vypadá následovně:\n");

        text.append("Budovy:\n");
        buildings.forEach(b -> text.append(b.toString()).append("\n"));
        text.append("\nPříroda okolo tebe:\n");
        naturals.forEach(n -> text.append(n.toString()).append("\n"));
        text.append("\nVyužití území:\n");
        landuses.forEach(l -> text.append(l.toString()).append("\n"));
        text.append("\nSilnice, cesty:\n");
        highways.forEach(h -> text.append(h.toString()).append("\n"));

        return text.toString();
    }

    public void computeCoords(double refLat, double refLon) {
        buildings.forEach(b -> computeNearestDistance(b.getNodeIds(), refLat, refLon, b::setDistance));
        buildings = buildings.stream()
                .sorted((b1, b2) -> {
                    double d1 = Double.parseDouble(b1.getDistance().replace("m", ""));
                    double d2 = Double.parseDouble(b2.getDistance().replace("m", ""));
                    return Double.compare(d1, d2);
                })
                .limit(3)
                .collect(Collectors.toList());

        naturals.forEach(n -> setCenter(n.getNodeIds(), n::setLon, n::setLat));
        landuses.forEach(l -> setCenter(l.getNodeIds(), l::setLon, l::setLat));
        highways.forEach(h -> setEndpoints(
                h.getNodeIds(),
                h::setStartLon, h::setStartLat,
                h::setEndLon,   h::setEndLat
        ));
    }

    public void computeNearestDistance(List<String> nodeIds, double refLat, double refLon, Consumer<String> distanceSetter) {
        double minDistance = Double.MAX_VALUE;
        for (String id : nodeIds) {
            Node node = nodeMap.get(id);
            if (node != null) {
                double nodeLat = Double.parseDouble(node.getLat());
                double nodeLon = Double.parseDouble(node.getLon());
                double distance = calculateDistance(refLat, refLon, nodeLat, nodeLon);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        distanceSetter.accept(minDistance + "m");
    }

    // Metoda pro výpočet vzdálenosti mezi dvěma body pomocí Haversineovy formule
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // Poloměr Země v metrech
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return  Math.round(R * c);
    }

    private void setCenter(
            List<String> ids,
            Consumer<String> lonSetter,
            Consumer<String> latSetter
    ) {
        double sumLon = 0, sumLat = 0;
        int count = 0;

        for (String id : ids) {
            Node node = nodeMap.get(id);
            if (node != null) {
                sumLon += Double.parseDouble(node.getLon());
                sumLat += Double.parseDouble(node.getLat());
                count++;
            }
        }

        if (count > 0) {
            lonSetter.accept(String.valueOf(sumLon / count));
            latSetter.accept(String.valueOf(sumLat / count));
        }
    }

    private void setEndpoints(
            List<String> ids,
            Consumer<String> startLonSetter,
            Consumer<String> startLatSetter,
            Consumer<String> endLonSetter,
            Consumer<String> endLatSetter
    ) {
        if (ids.isEmpty()) return;

        Node start = nodeMap.get(ids.getFirst());
        Node end   = nodeMap.get(ids.getLast());

        if (start != null) {
            startLonSetter.accept(start.getLon());
            startLatSetter.accept(start.getLat());
        }
        if (end != null) {
            endLonSetter.accept(end.getLon());
            endLatSetter.accept(end.getLat());
        }
    }

}
