package com.example.workflow.camunda.userTasks.rideBooking;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.userTasks.UserTask;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.utils.Constants;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class ReceiveSourceLocation implements UserTask {
    @Autowired
    CamundaCoreService camundaCoreService;

    @Autowired
    TemplateService templateService;

    @Autowired
    MessageService messageService;

    private final Logger logger = Logger.getLogger(ReceiveSourceLocation.class.getName());

    @Override
    public void complete(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        logger.info("Executing User Task " + this.getClass().getName());

        if (Objects.equals(messageType, Constants.MESSAGE_TYPE_LOCATION_REPLY)) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("source_latitude", webhookMessagePayload.getPayload().get("latitude"));
            variables.put("source_longitude", webhookMessagePayload.getPayload().get("longitude"));
            variables.put("select_favourite_place_pickup", false);
            camundaCoreService.completeUserTaskByTaskId(task, variables);
        } else if (Objects.equals(messageType, Constants.MESSAGE_TYPE_LIST_REPLY)) {
            String[] postBackResult = webhookMessagePayload.getPostbackText().split(":");

            Map<String, Object> variables = new HashMap<>();
            variables.put("source_latitude", postBackResult[0]);
            variables.put("source_longitude", postBackResult[1]);
            variables.put("select_favourite_place_pickup", false);
            camundaCoreService.completeUserTaskByTaskId(task, variables);
        } else {
            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), templateService.format(MessageTemplate.RIDE_INVALID_MESSAGE, user.getPreferredLanguage())));
        }
    }
}