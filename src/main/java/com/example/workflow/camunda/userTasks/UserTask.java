package com.example.workflow.camunda.userTasks;

import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import org.camunda.bpm.engine.task.Task;

public interface UserTask {
    public void complete(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception;
}
