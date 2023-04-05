package com.example.workflow.camunda.service.booking.userTasks;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.service.UserTask;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.WebhookMessagePayload;
import com.example.workflow.services.MessageService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiveDestinationUserTask implements UserTask {

    @Autowired
    MessageService messageService;

    @Autowired
    CamundaCoreService camundaCoreService;

    @Override
    public void execute(ActivityInstance activityInstance, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "Received location data "));
    }
}
