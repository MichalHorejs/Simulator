package com.gina.simulator.incident.dto;

import com.gina.simulator.enums.Category;
import lombok.Data;

@Data
public class IncidentResultsDTO {

    private Category chosenCategory;
    private Category correctCategory;
}
