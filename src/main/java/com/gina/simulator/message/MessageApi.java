package com.gina.simulator.message;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/simulation/incident")
@RequiredArgsConstructor
public class MessageApi {

    private final MessageService messageService;

    @PostMapping("message")
    public Message createMessage(@RequestBody Message message) {
        return messageService.create(message);
    }

    @GetMapping("{incidentId}/messages")
    public List<Message> getMessages(@PathVariable UUID incidentId) {
        return messageService.getMessages(incidentId);
    }
}
