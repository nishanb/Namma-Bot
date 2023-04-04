package com.example.workflow.services;

import com.example.workflow.dto.WebhookEventRequestDto;
import org.json.JSONException;

public interface MessageWebhookHandlerService {
    Boolean handleWebhookEvent(WebhookEventRequestDto webhookEventRequestDto) throws JSONException;
}
