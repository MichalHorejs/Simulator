package com.gina.simulator.simulation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/simulation")
@RequiredArgsConstructor
public class SimulationApi {

    private final SimulationService simulationService;

    @PostMapping("start")
    public Simulation startSimulation(
            @RequestBody Simulation simulation
    ) {
        return simulationService.startSimulation(simulation);
    }

    @PostMapping("finish")
    public Simulation finishSimulation(@RequestBody Simulation simulation) {
        return simulationService.finish(simulation);
    }
}
