package com.gina.simulator.incident;

import com.gina.simulator.enums.State;
import com.gina.simulator.exception.EntityNotFoundException;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.incidentTemplate.IncidentTemplateService;
import com.gina.simulator.integration.Osm.OsmService;
import com.gina.simulator.integration.Osm.features.Building;
import com.gina.simulator.integration.Osm.features.NearbyFeatures;
import com.gina.simulator.simulation.Simulation;
import com.gina.simulator.simulation.SimulationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
        log.info("Nearby features: {}", nearbyFeatures.getBuildings());

        Incident incident = new Incident();
        incident.setSimulation(simulation);
        incident.setPhoneNumber(generatePhoneNumber());
        incident.setState(State.INCOMING);
        incident.setStartTime(LocalDateTime.now());
        incident.setIncidentTemplate(incidentTemplate);
        incident.setContext(generateContext(nearbyFeatures, incidentTemplate));

        return incidentRepository.save(incident);
    }

    private String generatePhoneNumber() {
        Random random = new Random();
        int part1 = random.nextInt(1000);
        int part2 = random.nextInt(1000);
        int part3 = random.nextInt(1000);
        return String.format("+420 %03d %03d %03d", part1, part2, part3);
    }

    private String generateContext(NearbyFeatures nearbyFeatures, IncidentTemplate incidentTemplate) {
        List<Building> buildings = nearbyFeatures.getBuildings();

        StringBuilder sb = new StringBuilder();
        sb.append("Jsi civilista Karel Novák 43 let, který právě volá na hasičskou tísňovou linku. ");

        // Vlastnosti
        sb.append("Jsi průměrný civilista, který je chytrý, ale trochu stresuješ. ");

        // Omezeni
        sb.append("Neříkej přesné vzdálenosti. Vzdálenosti jsou uvedeny v metrech. Chovej se jako klasický občan");
        sb.append("volající na tisňovou linku. Neuvedené informace si domysli. Odpovídej v 1-3 kratších větách. ");
        sb.append("Odpovídej pouze na dotázané informace. Podle popisu co se stalo si vyber místo incidentu. "); // todo: jak resit lokalitu

        // Informace o incidentu
        sb.append("Detail incidentu které nesmíš říct. Kategorie: ").append(incidentTemplate.getCategory());
        sb.append(" podkategorie: ").append(incidentTemplate.getSubcategory());
        sb.append(". Tyhle musíš. okres: ").append(incidentTemplate.getAddress().getDistrict());
        sb.append(" vesnice/město: ").append(incidentTemplate.getAddress().getMunicipality());
        sb.append(" co se stalo: ").append(incidentTemplate.getSpecification());

        // Okoli
        sb.append(" Tvé okolí vypadá následovně: ");
        for (int i = 0; i < buildings.size(); i++) {
            sb.append("Budova ").append(i + 1).append(": ").append(buildings.get(i));
        }

        return sb.toString();
    }
}
