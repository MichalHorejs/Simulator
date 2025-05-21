package com.gina.simulator.incidentTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller handling incident template related requests.
 */
@RestController
@RequestMapping("api/incident-template")
@RequiredArgsConstructor
public class IncidentTemplateApi {

    private final IncidentTemplateService incidentTemplateService;

    @GetMapping("district")
    public List<String> findDistinctDistricts() {
        return incidentTemplateService.findDistinctDistricts();
    }

    @GetMapping("municipality")
    public List<String> findDistinctMunicipalities(@RequestParam("district") String districtName) {
        return incidentTemplateService.findDistinctMunicipalities(districtName);
    }
}
