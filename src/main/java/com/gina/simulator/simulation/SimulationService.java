package com.gina.simulator.simulation;

import com.gina.simulator.exception.BadRequestException;
import com.gina.simulator.person.Person;
import com.gina.simulator.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
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

    public boolean isActive(UUID simulationId) {
        Optional<Simulation> simOpt = simulationRepository.findById(simulationId);
        return simOpt.isPresent() && (simOpt.get().getEndTime() == null);
    }
}
