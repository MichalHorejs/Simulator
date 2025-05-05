package com.gina.simulator.message;

import com.gina.simulator.enums.Sender;
import com.gina.simulator.exception.EntityNotFoundException;
import com.gina.simulator.incident.Incident;
import com.gina.simulator.incident.IncidentRepository;
import com.gina.simulator.integration.OpenAi.OpenAiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final IncidentRepository incidentRepository;
    private final OpenAiClient openAiClient;

    @Transactional
    public Message create(Message message) {
        Incident incident = incidentRepository.findById(message.getIncident().getId())
                .orElseThrow(() -> new EntityNotFoundException(Incident.class, message.getIncident().getId()));

        message.setIncident(incident);
        messageRepository.save(message);

        List<Message> history = messageRepository.findAllByIncident_IdOrderByTimestampAsc(incident.getId());
        List<Map<String, String>> gptMessages = new ArrayList<>();
        gptMessages.add(Map.of("role", "system", "content", incident.getContext()));

        for (Message msg : history) {
            gptMessages.add(Map.of(
                    "role", msg.getSender() == Sender.USER ? "user" : "assistant",
                    "content", msg.getMessage()
            ));
        }

        String aiReply = openAiClient.askModel(gptMessages)
                .doOnError(err -> log.error("GPT error:", err))
                .doOnNext(reply -> log.info("GPT reply: {}", reply))
                .block();

        Message aiMessage = new Message();
        aiMessage.setIncident(incident);
        aiMessage.setMessage(aiReply);
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
