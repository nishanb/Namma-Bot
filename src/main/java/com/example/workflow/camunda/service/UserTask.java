package com.example.workflow.camunda.service;

import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.task.Task;

public interface UserTask {
    public void execute(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception;
}
