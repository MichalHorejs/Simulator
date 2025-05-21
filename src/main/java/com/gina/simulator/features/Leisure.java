package com.gina.simulator.features;

import com.gina.simulator.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for storing data about leisure time objects.
 */
@Data
public class Leisure {
    private String type;
    private String sport;
    private String distance;
    private List<String> nodeIds = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("typ: ").append(type).append(" ");
        if(Utils.empty(sport) != null) sb.append("sport: ").append(sport).append(" ");
        if(Utils.empty(distance) != null) sb.append("vzdálenost: ").append(distance).append(" ");

        return sb.toString();
    }
}
