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
    public ResponseEntity<Object> handleWebhookEvent(@RequestBody WebhookEventRequestDto webhookEventRequestDto) throws Exception {
        try {
            Boolean isMessageSent = messageServiceWebhookHandler.handleWebhookEvent(webhookEventRequestDto);
            return WorkflowServiceResponseDto.transformResponse("Webhook event handled", HttpStatus.OK, isMessageSent, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WorkflowServiceResponseDto.transformResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
        }

    }
}
