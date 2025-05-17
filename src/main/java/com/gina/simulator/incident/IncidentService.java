package com.gina.simulator.incident;

import com.gina.simulator.address.Address;
import com.gina.simulator.aiContext.AiContextGenerator;
import com.gina.simulator.enums.State;
import com.gina.simulator.exception.EntityNotFoundException;
import com.gina.simulator.features.NearbyFeatures;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.incidentTemplate.IncidentTemplateService;
import com.gina.simulator.integration.Osm.OsmService;
import com.gina.simulator.simulation.Simulation;
import com.gina.simulator.simulation.SimulationRepository;
import com.gina.simulator.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    @Value("${simulation.offset.meters:75}")
    private int offsetMeters;

    private final SimulationRepository simulationRepository;
    private final IncidentRepository incidentRepository;
    private final IncidentTemplateService incidentTemplateService;
    private final AiContextGenerator aiContextGenerator;
    private final OsmService osmService;

    @Transactional
    public Incident create(UUID simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new EntityNotFoundException(Simulation.class, simulationId));

        IncidentTemplate incidentTemplate = incidentTemplateService.findRandomTemplate();

        NearbyFeatures nearbyFeatures = osmService.generateNearbyFeatures(incidentTemplate);

        Incident incident = new Incident();

        if (incident.getAddress() == null) {
            incident.setAddress(new Address());
        }

        incident.setSimulation(simulation);
        incident.setPhoneNumber(generatePhoneNumber());
        incident.setState(State.INCOMING);
        incident.setStartTime(LocalDateTime.now());
        incident.setIncidentTemplate(incidentTemplate);
        incident.setContext(aiContextGenerator.generateContext(nearbyFeatures, incidentTemplate));

        incidentRepository.save(incident);

        return offsetIncidentCoords(incident);
    }

    @Transactional
    public Incident saveIncident(UUID incidentId, Incident incidentData) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new EntityNotFoundException(Incident.class, incidentId));

        if (incident.getAddress() == null) {
            incident.setAddress(new Address());
        }

        incident.setCategory(incidentData.getCategory());
        incident.setSubcategory(incidentData.getSubcategory());
        incident.setUrgency(incidentData.getUrgency());
        incident.getAddress().setDistrict(incidentData.getAddress().getDistrict());
        incident.getAddress().setMunicipality(incidentData.getAddress().getMunicipality());
        incident.getAddress().setLatitude(incidentData.getAddress().getLatitude());
        incident.getAddress().setLongitude(incidentData.getAddress().getLongitude());
        incident.setSpecification(incidentData.getSpecification());
        incident.setVehicleTypes(incidentData.getVehicleTypes());

        incident.setEndTime(LocalDateTime.now());
        incident.setState(State.FINISHED);

        return incidentRepository.save(incident);

    }

    @Transactional
    public void incidentPickedUp(UUID incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new EntityNotFoundException(Incident.class, incidentId));

        incident.setState(State.PROCESSED);
        incident.setCallPickedUpTime(LocalDateTime.now());
        incidentRepository.save(incident);
    }

    private Incident offsetIncidentCoords(Incident incident) {
        double lat = Double.parseDouble(incident.getIncidentTemplate().getAddress().getLatitude());
        double lon = Double.parseDouble(incident.getIncidentTemplate().getAddress().getLongitude());

        double[] ofsetCoords = Utils.offsetCoordinates(lat, lon, offsetMeters);
        incident.getAddress().setLatitude(String.valueOf(ofsetCoords[0]));
        incident.getAddress().setLongitude(String.valueOf(ofsetCoords[1]));

        return incident;
    }

    private String generatePhoneNumber() {
        Random random = new Random();
        int part1 = random.nextInt(1000);
        int part2 = random.nextInt(1000);
        int part3 = random.nextInt(1000);
        return String.format("+420 %03d %03d %03d", part1, part2, part3);
    }

}
