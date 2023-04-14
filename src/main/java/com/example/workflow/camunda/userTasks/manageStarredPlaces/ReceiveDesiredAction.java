package com.example.workflow.camunda.userTasks.manageStarredPlaces;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.service.starredPlace.ActionSelection;
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
public class ReceiveDesiredAction implements UserTask {
    @Autowired
    CamundaCoreService camundaCoreService;

    @Autowired
    TemplateService templateService;

    @Autowired
    MessageService messageService;

    private final Logger log = Logger.getLogger(ReceiveDesiredAction.class.getName());

    @Override
    public void complete(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        log.info("Executing User Task " + this.getClass().getName());

        if (Objects.equals(messageType, Constants.MESSAGE_TYPE_BUTTON_REPLY)) {
            String buttonPostbackText = webhookMessagePayload.getPostbackText();
            Map<String, Object> variables = new HashMap<>();
            variables.put("selection", buttonPostbackText);
            camundaCoreService.completeUserTaskByTaskId(task, variables);
        } else {
            log.warning("Invalid message received in user task : " + this.getClass().getName());
            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), templateService.format(MessageTemplate.RIDE_INVALID_MESSAGE, user.getPreferredLanguage())));
        }
    }
}