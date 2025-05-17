package com.gina.simulator.incident;

import com.gina.simulator.incident.dto.IncidentResultsDTO;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.utils.Utils;
import com.gina.simulator.vehicle.Vehicle;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IncidentMapper {

    public IncidentResultsDTO toIncidentResultsDTO(Incident incident) {
        IncidentTemplate template = incident.getIncidentTemplate();

        IncidentResultsDTO dto = new IncidentResultsDTO();
        dto.setChosenCategory(incident.getCategory());
        dto.setCorrectCategory(template.getCategory());
        dto.setChosenSubcategory(incident.getSubcategory());
        dto.setCorrectSubcategory(template.getSubcategory());
        dto.setChosenUrgency(incident.getUrgency());
        dto.setCorrectUrgency(template.getUrgency());
        dto.setChosenDistrict(incident.getAddress().getDistrict());
        dto.setCorrectDistrict(template.getAddress().getDistrict());
        dto.setChosenMuncipality(incident.getAddress().getMunicipality());
        dto.setCorrectMuncipality(template.getAddress().getMunicipality());

        double selLan = Double.parseDouble(incident.getAddress().getLatitude());
        double selLon = Double.parseDouble(incident.getAddress().getLongitude());
        double corLan = Double.parseDouble(template.getAddress().getLatitude());
        double corLon = Double.parseDouble(template.getAddress().getLongitude());

        dto.setDistance((int) Utils.calculateDistanceMeters(corLan, corLon, selLan, selLon));

        dto.setChosenVehicleTypes(incident.getVehicleTypes());
        dto.setCorrectVehicleTypes(template.getVehicles().stream()
                .map(Vehicle::getType)
                .collect(Collectors.toSet()));

        return dto;
    }
}
