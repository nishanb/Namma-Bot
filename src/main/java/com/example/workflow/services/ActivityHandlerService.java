package com.example.workflow.services;

import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import org.camunda.bpm.engine.task.Task;

public interface ActivityHandlerService {
    public void handle(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception;
}
