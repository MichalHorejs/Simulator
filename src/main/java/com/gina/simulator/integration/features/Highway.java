package com.gina.simulator.integration.features;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Highway {
    private String type;
    private String smoothness;
    private String surface;
    private String trackType;
    private String startLon;
    private String startLat;
    private String endLon;
    private String endLat;
    private List<String> nodeIds = new ArrayList<>();

    @Override
    public String toString() {
        return "* " +
                "typ='" + type + "'" +
                ", hladkost='" + smoothness + "'" +
                ", povrch='" + surface + "'" +
                ", pevnost/měkkost='" + trackType + "'" +
                ", souřadnice začátku cesty=" + startLon + "," + startLat +
                ", souřadnice konce cesty=" + endLon + "," + endLat;
    }
}
