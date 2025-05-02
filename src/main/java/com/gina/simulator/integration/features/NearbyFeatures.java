package com.gina.simulator.integration.features;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class NearbyFeatures {
    private Map<String, Node> nodeMap = new HashMap<>();
    private List<Building> buildings = new ArrayList<>();
    private List<Natural> naturals = new ArrayList<>();
}
