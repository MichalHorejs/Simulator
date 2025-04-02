package com.gina.simulator.incident;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/simulation/")
@RequiredArgsConstructor
public class IncidentApi {

    private final IncidentService incidentService;

    @PostMapping("{simulationId}/incident")
    public Incident createIncident(@PathVariable UUID simulationId) {
        return incidentService.create(simulationId);
    }
}
