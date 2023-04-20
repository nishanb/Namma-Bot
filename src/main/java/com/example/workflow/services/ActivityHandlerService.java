package com.example.workflow.services;

import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import org.camunda.bpm.engine.task.Task;

public interface ActivityHandlerService {
    void handle(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception;

    void handleCancelRequest(User user, String businessKey, String processDefinitionName) throws Exception;
}
