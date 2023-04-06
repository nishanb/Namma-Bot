package com.example.workflow.serviceImpl.activityHandlers;

import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.services.ActivityHandlerService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
public class LanguageChangeActivityHandler implements ActivityHandlerService {
    @Override
    public void handle(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {

    }
}
