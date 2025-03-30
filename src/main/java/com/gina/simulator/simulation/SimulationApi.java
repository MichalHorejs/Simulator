package com.gina.simulator.simulation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/simulation")
@RequiredArgsConstructor
public class SimulationApi {

    private final SimulationService simulationService;

    @GetMapping("active/{id}")
    public boolean active(@PathVariable UUID id) {
        return simulationService.isActive(id);
    }

    @PostMapping("start")
    public Simulation startSimulation(
            @RequestBody Simulation simulation
    ) {
        return simulationService.startSimulation(simulation);
    }
}
