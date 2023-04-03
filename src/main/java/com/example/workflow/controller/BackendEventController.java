package com.example.workflow.controller;

import com.example.workflow.dto.BackendEventRequestDto;
import com.example.workflow.service.BackendEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Webhook to accept namma yatri backend events on cws side

@RestController
@RequestMapping("/api/v1")
public class BackendEventController {
    @Autowired
    private BackendEventHandler eventWebhookHandlerService;

    @PostMapping("/backend-events")
    public boolean createEvent(@RequestBody BackendEventRequestDto event) {
        return eventWebhookHandlerService.handelEvent(event);
    }
}
