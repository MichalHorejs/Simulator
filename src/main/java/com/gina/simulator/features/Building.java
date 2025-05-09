package com.gina.simulator.features;

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
            sb.append("typ: ").append(type).append(" ");
        }
        if (Utils.empty(flats) != null) {
            sb.append("počet bytů: ").append(flats).append(" ");
        }
        if (Utils.empty(levels) != null) {
            sb.append("počet pater: ").append(levels).append(" ");
        }
        if (Utils.empty(houseNumber) != null) {
            sb.append("číslo domu: ").append(houseNumber).append(" ");
        } if (Utils.empty(distance) != null) {
            sb.append("vzdálenost: ").append(distance);
        }
        return sb.toString();
    }
}
