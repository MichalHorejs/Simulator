package com.gina.simulator.simulation;

import com.gina.simulator.incident.IncidentMapper;
import com.gina.simulator.incident.dto.IncidentResultsDTO;
import com.gina.simulator.simulation.dto.SimulationResultsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SimulationMapper {

    private final IncidentMapper incidentMapper;

    public SimulationResultsDTO toSimulationResultsDTO(Simulation simulation, int score) {
        SimulationResultsDTO dto = new SimulationResultsDTO();

        dto.setUsername(simulation.getPerson().getUsername());
        dto.setResult(score);
        dto.setDifficulty(simulation.getDifficulty());

        List<IncidentResultsDTO> incidentResultsDTOS = simulation.getIncidents().stream()
                .map(incidentMapper::toIncidentResultsDTO)
                .toList();

        dto.setIncidents(incidentResultsDTOS);

        return dto;
    }
}
