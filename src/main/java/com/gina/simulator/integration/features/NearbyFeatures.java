package com.gina.simulator.integration.features;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    public void computeCoords() {
        buildings.forEach(b -> setCenter(b.getNodeIds(), b::setLon, b::setLat));
        naturals.forEach(n -> setCenter(n.getNodeIds(), n::setLon, n::setLat));
        landuses.forEach(l -> setCenter(l.getNodeIds(), l::setLon, l::setLat));
        highways.forEach(h -> setEndpoints(
                h.getNodeIds(),
                h::setStartLon, h::setStartLat,
                h::setEndLon,   h::setEndLat
        ));
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
