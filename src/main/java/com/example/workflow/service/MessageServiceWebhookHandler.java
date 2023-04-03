package com.example.workflow.service;

import com.example.workflow.dto.WebhookEventRequestDto;
import org.json.JSONException;
import org.json.JSONObject;

public interface MessageServiceWebhookHandler {
    Boolean handleWebhookEvent(WebhookEventRequestDto webhookEventRequestDto) throws JSONException;
}
