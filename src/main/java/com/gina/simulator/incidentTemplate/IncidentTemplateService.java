package com.gina.simulator.incidentTemplate;

import com.gina.simulator.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentTemplateService {

    private final IncidentTemplateRepository incidentTemplateRepository;

    public IncidentTemplate findRandomTemplate(){
        return incidentTemplateRepository.findById(UUID.fromString("f9bd4ce3-0d25-46f8-b093-a3b60d66559b"))
                .orElseThrow(() -> new EntityNotFoundException(IncidentTemplate.class));
//        todo: uncomment
//        return incidentTemplateRepository.findRandomTemplate()
//                .orElseThrow(() -> new EntityNotFoundException(IncidentTemplate.class));
    }
}
