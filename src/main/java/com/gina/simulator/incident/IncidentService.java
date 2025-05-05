package com.gina.simulator.incident;

import com.gina.simulator.enums.State;
import com.gina.simulator.exception.EntityNotFoundException;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.incidentTemplate.IncidentTemplateService;
import com.gina.simulator.integration.Osm.OsmService;
import com.gina.simulator.integration.features.NearbyFeatures;
import com.gina.simulator.simulation.Simulation;
import com.gina.simulator.simulation.SimulationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final SimulationRepository simulationRepository;
    private final IncidentRepository incidentRepository;
    private final IncidentTemplateService incidentTemplateService;
    private final OsmService osmService;

    @Transactional
    public Incident create(UUID simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new EntityNotFoundException(Simulation.class, simulationId));

        IncidentTemplate incidentTemplate = incidentTemplateService.findRandomTemplate();

        NearbyFeatures nearbyFeatures = osmService.generateNearbyFeatures(incidentTemplate);

        Incident incident = new Incident();
        incident.setSimulation(simulation);
        incident.setPhoneNumber(generatePhoneNumber());
        incident.setState(State.INCOMING);
        incident.setStartTime(LocalDateTime.now());
        incident.setIncidentTemplate(incidentTemplate);
        incident.setContext(nearbyFeatures.toString());

        return incidentRepository.save(incident);
    }

    private String generatePhoneNumber() {
        Random random = new Random();
        int part1 = random.nextInt(1000);
        int part2 = random.nextInt(1000);
        int part3 = random.nextInt(1000);
        return String.format("+420 %03d %03d %03d", part1, part2, part3);
    }
}
