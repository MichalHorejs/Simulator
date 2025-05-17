package com.gina.simulator.incident;

import com.gina.simulator.incident.dto.IncidentResultsDTO;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.utils.Utils;
import com.gina.simulator.vehicle.Vehicle;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class IncidentMapper {

    public IncidentResultsDTO toIncidentResultsDTO(Incident incident) {

        IncidentResultsDTO dto = new IncidentResultsDTO();
        IncidentTemplate template = incident.getIncidentTemplate();

        dto.setCorrectCategory(template.getCategory().getDisplayName());
        dto.setCorrectSubcategory(template.getSubcategory().getDisplayName());
        dto.setCorrectUrgency(template.getUrgency());
        dto.setCorrectDistrict(template.getAddress().getDistrict());
        dto.setCorrectMuncipality(template.getAddress().getMunicipality());
        dto.setCorrectVehicleTypes(template.getVehicles().stream()
                .map(Vehicle::getType)
                .collect(Collectors.toSet()));

        if (incident.getCategory() == null ||
                incident.getSubcategory() == null ||
                incident.getUrgency() == null ||
                incident.getAddress() == null ||
                incident.getAddress().getDistrict() == null ||
                incident.getAddress().getMunicipality() == null ||
                incident.getAddress().getLatitude() == null ||
                incident.getAddress().getLongitude() == null ||
                incident.getIncidentTemplate() == null ||
                incident.getStartTime() == null ||
                incident.getCallPickedUpTime() == null ||
                incident.getEndTime() == null) {
            return dto;
        }

        dto.setChosenCategory(incident.getCategory().getDisplayName());
        dto.setChosenSubcategory(incident.getSubcategory().getDisplayName());
        dto.setChosenUrgency(incident.getUrgency());
        dto.setChosenDistrict(incident.getAddress().getDistrict());
        dto.setChosenMuncipality(incident.getAddress().getMunicipality());

        double selLan = Double.parseDouble(incident.getAddress().getLatitude());
        double selLon = Double.parseDouble(incident.getAddress().getLongitude());
        double corLan = Double.parseDouble(template.getAddress().getLatitude());
        double corLon = Double.parseDouble(template.getAddress().getLongitude());

        dto.setDistance((int) Utils.calculateDistanceMeters(corLan, corLon, selLan, selLon));

        dto.setChosenVehicleTypes(incident.getVehicleTypes());

        LocalDateTime startTime = incident.getStartTime();
        LocalDateTime endTime = incident.getEndTime();
        LocalDateTime callPickedUpTime = incident.getCallPickedUpTime();

        dto.setDurationToPickUp((int) Duration.between(startTime, callPickedUpTime).getSeconds());
        dto.setDurationToServeIncident((int) Duration.between(callPickedUpTime, endTime).getSeconds());

        return dto;
    }
}
