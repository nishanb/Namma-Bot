package com.example.workflow.controller;

import com.example.workflow.dto.BackendEventRequestDto;
import com.example.workflow.dto.WebhookEventRequestDto;
import com.example.workflow.dto.WorkflowServiceResponseDto;
import com.example.workflow.service.BackendEventHandler;
import com.example.workflow.service.MessageService;
import com.example.workflow.service.MessageServiceWebhookHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Webhook to accept namma yatri backend events on cws side

@RestController
@RequestMapping("/api/v1")
public class MessageWebhookController {
    @Autowired
    MessageServiceWebhookHandler messageServiceWebhookHandler;

    @PostMapping("/message/webhook")
    public void handleWebhookEvent(@RequestBody WebhookEventRequestDto webhookEventRequestDto) throws Exception {
        messageServiceWebhookHandler.handleWebhookEvent(webhookEventRequestDto);
    }
}
