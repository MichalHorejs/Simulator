package com.gina.simulator.features;

import com.gina.simulator.utils.Utils;
import lombok.Data;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class responsible for storing data about incident surrounding.
 */
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

        buildings = sortAndLimit(buildings, Building::getDistance);
        leisures = sortAndLimit(leisures, Leisure::getDistance);
        amenities = sortAndLimit(amenities, Amenity::getDistance);
    }

    private <T> List<T> sortAndLimit(List<T> list, Function<T, String> getDistance) {
        return list.stream()
                .sorted(Comparator.comparingDouble(item ->
                        Double.parseDouble(getDistance.apply(item).replace("m", ""))))
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
