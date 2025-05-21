package com.gina.simulator.features;

import com.gina.simulator.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for storing data about amenities.
 */
@Data
public class Amenity {
    private String type;
    private String name;
    private Boolean bench;
    private String building;
    private String buildingType;
    private String shelter_type;

    private String distance;
    private List<String> nodeIds = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("typ: ").append(type).append(" ");
        if(Utils.empty(name) != null) sb.append("název: ").append(name).append(" ");
        if(bench) sb.append("je zde lavice").append(" ");
        if(Utils.empty(building) != null) sb.append("druh budovy: ").append(building).append(" ");
        if(Utils.empty(buildingType) != null) sb.append("typ budovy: ").append(buildingType).append(" ");
        if(Utils.empty(shelter_type) != null) sb.append("typ přístřešku: ").append(shelter_type).append(" ");
        if(Utils.empty(distance) != null) sb.append("vzdálenost: ").append(distance).append(" ");

        return sb.toString();
    }
}
