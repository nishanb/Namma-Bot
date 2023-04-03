package com.example.workflow.dto;

import com.example.workflow.models.WebhookMessagePayload;

public class WebhookEventRequestDto {
    private String type;

    private WebhookMessagePayload webhookMessagePayload;

    public WebhookEventRequestDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WebhookMessagePayload getWebhookMessagePayload() {
        return webhookMessagePayload;
    }

    public void setPayload(WebhookMessagePayload webhookMessagePayload) {
        this.webhookMessagePayload = webhookMessagePayload;
    }
}
