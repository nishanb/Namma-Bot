package com.example.workflow.serviceImpl.activityHandlers;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.userTasks.manageStarredPlaces.ReceiveDesiredAction;
import com.example.workflow.camunda.userTasks.manageStarredPlaces.ReceiveLocationTagToAdd;
import com.example.workflow.camunda.userTasks.manageStarredPlaces.ReceiveLocationToAdd;
import com.example.workflow.camunda.userTasks.manageStarredPlaces.ReceiveLocationToDelete;
import com.example.workflow.config.BpmnUserTask;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.serviceImpl.CommonMessageService;
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
public class StarredPlaceManageActivityHandler implements ActivityHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(StarredPlaceManageActivityHandler.class);
    @Autowired
    CamundaCoreService camundaCoreService;
    @Autowired
    ReceiveDesiredAction receiveDesiredAction;
    @Autowired
    ReceiveLocationToAdd receiveLocationToAdd;
    @Autowired
    ReceiveLocationTagToAdd receiveLocationTagToAdd;
    @Autowired
    ReceiveLocationToDelete receiveLocationToDelete;
    @Autowired
    CommonMessageService commonMessageService;

    @Override
    public void handle(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        logger.info(String.format("Handling User Task -> " + task.getName() + " For user -> " + user.getPhoneNumber() + " in Starred Places Workflow"));

        switch (BpmnUserTask.fromTaskDefinitionKey(task.getTaskDefinitionKey())) {
            case STARRED_PLACE_DESIRED_ACTION -> {
                receiveDesiredAction.complete(task, user, messageType, webhookMessagePayload);
            }
            case STARRED_PLACE_RECEIVE_LOCATION_TO_ADD -> {
                receiveLocationToAdd.complete(task, user, messageType, webhookMessagePayload);
            }
            case STARRED_PLACE_RECEIVE_LOCATION_TAG_TO_ADD -> {
                receiveLocationTagToAdd.complete(task, user, messageType, webhookMessagePayload);
            }
            case STARRED_PLACE_RECEIVE_LOCATION_TO_DELETE -> {
                receiveLocationToDelete.complete(task, user, messageType, webhookMessagePayload);
            }
            case default -> {
                commonMessageService.sendErrorMessage(user);
                logger.info(String.format(">>>>>>>> No user task class found for %s <<<<<<<<<", task.getName()));
            }
        }
    }

    @Override
    public void handleCancelRequest(User user, String businessKey, String processDefinitionName) throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("global_cancellation", true);
        String cancelMessageEventName = GLOBAL_CANCELLATION_MESSAGE_EVENT_NAME.get(processDefinitionName);
        camundaCoreService.createMessageCorrelation(businessKey, cancelMessageEventName, variables);
    }
}