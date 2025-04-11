package com.gina.simulator.message;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/simulation/incident")
@RequiredArgsConstructor
public class MessageApi {

    private final MessageService messageService;

    @PostMapping("message")
    public Message createMessage(@RequestBody Message message) {
        return messageService.create(message);
    }
}
