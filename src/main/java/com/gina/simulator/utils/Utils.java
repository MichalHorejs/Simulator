package com.gina.simulator.utils;

public class Utils {

    public static String empty(String s) {
        return s == null || s.isBlank() ? null : s;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }
}
