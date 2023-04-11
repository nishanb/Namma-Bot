package com.example.workflow.serviceImpl.activityHandlers;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.userTasks.lanugaeUpdate.ReceiveLanguageConfirmation;
import com.example.workflow.camunda.userTasks.lanugaeUpdate.ReceiveLanguagePreference;
import com.example.workflow.config.BpmnUserTask;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.services.ActivityHandlerService;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.example.workflow.utils.Constants.GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME;

@Service
public class LanguageChangeActivityHandler implements ActivityHandlerService {
    @Autowired
    ReceiveLanguageConfirmation receiveLanguageConfirmation;
    @Autowired
    ReceiveLanguagePreference receiveLanguagePreference;

    @Autowired
    CamundaCoreService camundaCoreService;
    private static final Logger logger = LoggerFactory.getLogger(RideBookingActivityHandler.class);

    @Override
    public void handle(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        System.out.println("in lang update task");
        switch (BpmnUserTask.fromTaskDefinitionKey(task.getTaskDefinitionKey())) {
            case LANGUAGE_UPDATE_PREFERENCE_SELECTION -> {
                receiveLanguagePreference.complete(task, user, messageType, webhookMessagePayload);
            }
            case LANGUAGE_UPDATE_PREFERENCE_CONFIRMATION -> {
                receiveLanguageConfirmation.complete(task, user, messageType, webhookMessagePayload);
            }
            case default -> {
                logger.info(String.format(">>>>>>>> No user task class found for %s <<<<<<<<<", task.getName()));
            }
        }
    }

    @Override
    public void handleCancelRequest(User user, String businessKey, String processDefinitionName) throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("global_cancellation",true);
        String cancelMessageEventName = GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.get(processDefinitionName);
        camundaCoreService.createMessageCorrelation(businessKey,cancelMessageEventName, variables);
    }
}
