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
        return incidentTemplateRepository.findById(UUID.fromString("03cf5e9d-4bda-4691-9b17-67f473f005cb"))
                .orElseThrow(() -> new EntityNotFoundException(IncidentTemplate.class));
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
