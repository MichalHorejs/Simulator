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
