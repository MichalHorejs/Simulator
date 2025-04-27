package com.gina.simulator.message;

import com.gina.simulator.enums.Sender;
import com.gina.simulator.exception.EntityNotFoundException;
import com.gina.simulator.incident.Incident;
import com.gina.simulator.incident.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final IncidentRepository incidentRepository;

    @Transactional
    public Message create(Message message) {
        Incident incident = incidentRepository.findById(message.getIncident().getId())
                .orElseThrow(() -> new EntityNotFoundException(Incident.class, message.getIncident().getId()));

        message.setIncident(incident);
        messageRepository.save(message);

        Message aiMessage = new Message();
        aiMessage.setIncident(incident);
        aiMessage.setMessage("from AI");
        aiMessage.setSender(Sender.AI);
        aiMessage.setTimestamp(LocalDateTime.now());

        return messageRepository.save(aiMessage);
    }

    public List<Message> getMessages(UUID incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new EntityNotFoundException(Incident.class, incidentId));

        return messageRepository.findAllByIncident_IdOrderByTimestampAsc(incident.getId());
    }
}
