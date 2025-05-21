package com.gina.simulator.features;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class responsible for storing data about incident coords.
 */
@Data
@AllArgsConstructor
public class Node {
    private String lon;
    private String lat;
}
