package com.gina.simulator.utils;

import java.util.Random;

public class Utils {
    private static final double METERS_PER_DEGREE_LAT = 111_320;

    public static String empty(String s) {
        return s == null || s.isBlank() ? null : s;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }

    // haversine
    public static double calculateDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // poloměr Země v metrech
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static double[] offsetCoordinates(double lat, double lon, int maxOffsetMeters) {
        Random random = new Random();
        double distance = random.nextDouble() * maxOffsetMeters;
        double angle = random.nextDouble() * 2 * Math.PI;

        double deltaX = distance * Math.cos(angle);
        double deltaY = distance * Math.sin(angle);

        double deltaLat = deltaY / METERS_PER_DEGREE_LAT;
        double metersPerDegreeLon = METERS_PER_DEGREE_LAT * Math.cos(Math.toRadians(lat));
        double deltaLon = deltaX / metersPerDegreeLon;

        return new double[]{ lat + deltaLat, lon + deltaLon };
    }
}
