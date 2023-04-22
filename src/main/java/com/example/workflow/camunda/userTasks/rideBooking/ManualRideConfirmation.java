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
public class ManualRideConfirmation implements UserTask {

    @Autowired
    CamundaCoreService camundaCoreService;

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    private final Logger logger = Logger.getLogger(ManualRideConfirmation.class.getName());

    @Override
    public void complete(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        logger.info("Executing User Task " + this.getClass().getName());

        if (Objects.equals(messageType, Constants.MESSAGE_TYPE_LIST_REPLY)) {
            //Post back text has both ID and name. TODO: Fetch the name from Drivers entity with ID.
            String chosenDriverId = webhookMessagePayload.getPostbackText();
            Map<String, Object> variables = new HashMap<>();
            variables.put("chosen_driver_id", chosenDriverId);
            variables.put("user_selected_ride", true);
            camundaCoreService.completeUserTaskByTaskId(task, variables);
        } else {
            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), templateService.format(MessageTemplate.RIDE_INVALID_MESSAGE, user.getPreferredLanguage())));
        }
    }
}
