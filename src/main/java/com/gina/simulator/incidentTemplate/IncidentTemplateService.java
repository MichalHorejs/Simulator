package com.gina.simulator.incidentTemplate;

import com.gina.simulator.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentTemplateService {

    private final IncidentTemplateRepository incidentTemplateRepository;

    public IncidentTemplate findRandomTemplate(){
        return incidentTemplateRepository.findRandomTemplate()
                .orElseThrow(() -> new EntityNotFoundException(IncidentTemplate.class));
    }

    public List<String> findDistinctDistricts() {
        return incidentTemplateRepository.findDistinctDistricts();
    }

    public List<String> findDistinctMunicipalities(String districtName) {
        return incidentTemplateRepository.findDistinctMunicipalities(districtName);
    }
}
