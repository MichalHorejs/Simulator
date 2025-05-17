package com.gina.simulator.incident.dto;

import com.gina.simulator.enums.Urgency;
import com.gina.simulator.enums.VehicleType;
import lombok.Data;

import java.util.Set;

@Data
public class IncidentResultsDTO {

    private String chosenCategory;
    private String correctCategory;

    private String chosenSubcategory;
    private String correctSubcategory;

    private Urgency chosenUrgency;
    private Urgency correctUrgency;

    private String chosenDistrict;
    private String correctDistrict;

    private String chosenMuncipality;
    private String correctMuncipality;

    private Set<VehicleType> chosenVehicleTypes;
    private Set<VehicleType> correctVehicleTypes;

    private Integer distance;

    private Integer durationToPickUp;
    private Integer durationToServeIncident;
}
