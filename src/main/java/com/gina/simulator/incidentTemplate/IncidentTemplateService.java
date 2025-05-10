package com.gina.simulator.incidentTemplate;

import com.gina.simulator.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentTemplateService {

    private final IncidentTemplateRepository incidentTemplateRepository;

    public IncidentTemplate findRandomTemplate(){
        return incidentTemplateRepository.findById(UUID.fromString("24cfc3e7-c4e1-494f-9e2f-7c90d3c4d3aa"))
                .orElseThrow(() -> new EntityNotFoundException(IncidentTemplate.class));
//        todo: uncomment
//        return incidentTemplateRepository.findRandomTemplate()
//                .orElseThrow(() -> new EntityNotFoundException(IncidentTemplate.class));
    }

    public List<String> findDistinctDistricts() {
        return incidentTemplateRepository.findDistinctDistricts();
    }

    public List<String> findDistinctMunicipalities(String districtName) {
        return incidentTemplateRepository.findDistinctMunicipalities(districtName);
    }
}
