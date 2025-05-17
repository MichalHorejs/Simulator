package com.gina.simulator.incident;

import com.gina.simulator.incident.dto.IncidentResultsDTO;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import org.springframework.stereotype.Component;

@Component
public class IncidentMapper {

    public IncidentResultsDTO toIncidentResultsDTO(Incident incident) {
        IncidentTemplate template = incident.getIncidentTemplate();

        IncidentResultsDTO dto = new IncidentResultsDTO();
        dto.setChosenCategory(incident.getCategory());
        dto.setCorrectCategory(template.getCategory());

        return dto;
    }
}
