package com.gina.simulator.incident;

import com.gina.simulator.enums.State;
import com.gina.simulator.exception.BadRequestException;
import com.gina.simulator.simulation.Simulation;
import com.gina.simulator.simulation.SimulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final SimulationRepository simulationRepository;
    private final IncidentRepository incidentRepository;

    @Transactional
    public Incident create(UUID simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new BadRequestException("Simulation not found in DB."));

        Incident incident = new Incident();
        incident.setSimulation(simulation);
        incident.setPhoneNumber(generatePhoneNumber());
        incident.setState(State.INCOMING);
        incident.setStartTime(LocalDateTime.now());

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
