package com.gina.simulator.features;

import com.gina.simulator.utils.Utils;
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
    private List<Leisure> leisures = new ArrayList<>();
    private List<Amenity> amenities = new ArrayList<>();


    public void computeCoords(double refLat, double refLon) {
        buildings.forEach(b -> computeNearestDistance(b.getNodeIds(), refLat, refLon, b::setDistance));
        leisures.forEach(l -> computeNearestDistance(l.getNodeIds(), refLat, refLon, l::setDistance));
        amenities.forEach(a -> computeNearestDistance(a.getNodeIds(), refLat, refLon, a::setDistance));

        buildings = buildings.stream()
                .sorted((b1, b2) -> {
                    double d1 = Double.parseDouble(b1.getDistance().replace("m", ""));
                    double d2 = Double.parseDouble(b2.getDistance().replace("m", ""));
                    return Double.compare(d1, d2);
                })
                .limit(3)
                .collect(Collectors.toList());

        leisures = leisures.stream()
                .sorted((l1, l2) -> {
                    double d1 = Double.parseDouble(l1.getDistance().replace("m", ""));
                    double d2 = Double.parseDouble(l2.getDistance().replace("m", ""));
                    return Double.compare(d1, d2);
                })
                .limit(3)
                .collect(Collectors.toList());

        amenities = amenities.stream()
                .sorted((a1, a2) -> {
                    double d1 = Double.parseDouble(a1.getDistance().replace("m", ""));
                    double d2 = Double.parseDouble(a2.getDistance().replace("m", ""));
                    return Double.compare(d1, d2);
                })
                .limit(3)
                .collect(Collectors.toList());

    }

    public void computeNearestDistance(List<String> nodeIds, double refLat, double refLon, Consumer<String> distanceSetter) {
        double minDistance = Double.MAX_VALUE;
        for (String id : nodeIds) {
            Node node = nodeMap.get(id);
            if (node != null) {
                double nodeLat = Double.parseDouble(node.getLat());
                double nodeLon = Double.parseDouble(node.getLon());
                double distance = Utils.calculateDistanceMeters(refLat, refLon, nodeLat, nodeLon);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        distanceSetter.accept(minDistance + "m");
    }

}
