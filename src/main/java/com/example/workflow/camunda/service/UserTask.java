package com.example.workflow.camunda.service;

import com.example.workflow.models.User;
import com.example.workflow.models.WebhookMessagePayload;
import org.camunda.bpm.engine.runtime.ActivityInstance;

public interface UserTask {
    public void execute(ActivityInstance activityInstance, User user,String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception;
}
