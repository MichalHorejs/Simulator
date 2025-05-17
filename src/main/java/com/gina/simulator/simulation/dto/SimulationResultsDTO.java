package com.gina.simulator.simulation.dto;

import com.gina.simulator.enums.Difficulty;
import com.gina.simulator.incident.dto.IncidentResultsDTO;
import lombok.Data;

import java.util.List;

@Data
public class SimulationResultsDTO {

    private String username;
    private int result;
    private Difficulty difficulty;
    private List<IncidentResultsDTO> incidents;
}
