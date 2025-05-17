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
        return incidentTemplateRepository.findById(UUID.fromString("bf2d92d3-cc9b-4250-8100-d41104d605b9"))
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
