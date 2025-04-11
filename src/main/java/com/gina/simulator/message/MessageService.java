package com.gina.simulator.message;

import com.gina.simulator.exception.BadRequestException;
import com.gina.simulator.incident.Incident;
import com.gina.simulator.incident.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final IncidentRepository incidentRepository;

    @Transactional
    public Message create(Message message) {
        Incident incident = incidentRepository.findById(message.getIncident().getId())
                .orElseThrow(() -> new BadRequestException("Incident not found in DB."));

        message.setIncident(incident);
        return messageRepository.save(message);
    }
}
