package com.gina.simulator.integration.Osm.features;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Natural {
    private String type;
    private String lon;
    private String lat;
    private List<String> nodeIds = new ArrayList<>();

    @Override
    public String toString() {
        return "* " +
                "typ='" + type + "'" +
                ", souřadnice=" + lon + "," + lat;
    }
}
