package com.gina.simulator.incident.dto;

import com.gina.simulator.enums.Category;
import com.gina.simulator.enums.Subcategory;
import com.gina.simulator.enums.Urgency;
import com.gina.simulator.enums.VehicleType;
import lombok.Data;

import java.util.Set;

@Data
public class IncidentResultsDTO {

    private Category chosenCategory;
    private Category correctCategory;

    private Subcategory chosenSubcategory;
    private Subcategory correctSubcategory;

    private Urgency chosenUrgency;
    private Urgency correctUrgency;

    private String chosenDistrict;
    private String correctDistrict;

    private String chosenMuncipality;
    private String correctMuncipality;

    private Set<VehicleType> chosenVehicleTypes;
    private Set<VehicleType> correctVehicleTypes;

    private int distance;

    private int durationToPickUp;
    private int durationToServeIncident;
}
