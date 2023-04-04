package com.example.workflow.controllers;

import com.example.workflow.dto.WebhookEventRequestDto;
import com.example.workflow.services.MessageWebhookHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Webhook to accept namma yatri backend events on cws side

@RestController
@RequestMapping("/api/v1")
public class MessageWebhookController {
    @Autowired
    MessageWebhookHandlerService messageWebhookHandlerService;

    @PostMapping("/message/webhook")
    public void handleWebhookEvent(@RequestBody WebhookEventRequestDto webhookEventRequestDto) throws Exception {
        messageWebhookHandlerService.handleWebhookEvent(webhookEventRequestDto);
    }
}
