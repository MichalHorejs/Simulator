package com.gina.simulator.integration.Osm.features;

import com.gina.simulator.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Building {
    private String type;
    private String flats;
    private String levels;
    private String houseNumber;
    private String distance;
    private List<String> nodeIds = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (Utils.empty(type) != null) {
            sb.append("Typ: ").append(type).append(" ");
        }
        if (Utils.empty(flats) != null) {
            sb.append("Počet bytů: ").append(flats).append(" ");
        }
        if (Utils.empty(levels) != null) {
            sb.append("Počet pater: ").append(levels).append(" ");
        }
        if (Utils.empty(houseNumber) != null) {
            sb.append("Číslo domu: ").append(houseNumber).append(". ");
        }
        return sb.toString();
    }
}
