package com.gina.simulator.simulation;

import com.gina.simulator.simulation.dto.SimulationResultsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller handling simulation related requests.
 */
@RestController
@RequestMapping("api/simulation")
@RequiredArgsConstructor
public class SimulationApi {

    private final SimulationService simulationService;

    @PostMapping("start")
    public Simulation startSimulation(
            @RequestBody Simulation simulation
    ) {
        System.out.println(simulation);
        return simulationService.startSimulation(simulation);
    }

    @PostMapping("finish")
    public void finishSimulation(@RequestBody Simulation simulation) {
        simulationService.finish(simulation);
    }

    @GetMapping("{simulationId}/detail")
    public SimulationResultsDTO getSimulationDetails(@PathVariable UUID simulationId) {
        return simulationService.getSimulatinDetails(simulationId);
    }
}
