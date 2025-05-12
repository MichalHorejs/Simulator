package com.gina.simulator.enums.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleTypeDTO {
    private String name;
    private String displayName;
    private String category;
}
