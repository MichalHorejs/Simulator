package com.gina.simulator.simulation;

import com.gina.simulator.exception.BadRequestException;
import com.gina.simulator.person.Person;
import com.gina.simulator.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final PersonRepository personRepository;
    private final SimulationRepository simulationRepository;

    @Transactional
    public Simulation startSimulation(Simulation simulation) {
        Person person = personRepository.findByUsername(simulation.getPerson().getUsername())
                .orElseThrow(() -> new BadRequestException("User not found in DB."));

        simulation.setStartTime(LocalDateTime.now());
        simulation.setPerson(person);
        return simulationRepository.save(simulation);
    }

    public boolean isActive(UUID simulationId) { // todo: think if will be used
        Optional<Simulation> simOpt = simulationRepository.findById(simulationId);
        return simOpt.isPresent() && (simOpt.get().getEndTime() == null);
    }

    @Transactional
    public Simulation finish(Simulation s) {
        Simulation simulation = simulationRepository.findById(s.getId())
                .orElseThrow(() -> new BadRequestException("Simulation not found in DB."));
        simulation.setEndTime(LocalDateTime.now());
        simulation.setRating(new Random().nextInt(1000) + 1); // todo: compute
        return simulationRepository.save(simulation);
    }
}
