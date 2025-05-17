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

    @PostMapping("/incident/{incidentId}/save")
    public Incident saveIncident(@PathVariable UUID incidentId, @RequestBody Incident incidentData) {
        return incidentService.saveIncident(incidentId, incidentData);
    }

    @PostMapping("/incident/{incidentId}/picked-up")
    public void incidentPickedUp(@PathVariable UUID incidentId) {
        incidentService.incidentPickedUp(incidentId);
    }
}
