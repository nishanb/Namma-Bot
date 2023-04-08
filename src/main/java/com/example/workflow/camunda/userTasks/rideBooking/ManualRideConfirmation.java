package com.example.workflow.camunda.userTasks.rideBooking;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.userTasks.UserTask;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.utils.Constants;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ManualRideConfirmation implements UserTask {

    @Autowired
    CamundaCoreService camundaCoreService;

    @Override
    public void complete(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        if (Objects.equals(messageType, Constants.MESSAGE_TYPE_LIST_REPLY)) {
            //Post back text has both ID and name. TODO: Fetch the name from Drivers entity with ID.
            String chosenDriverId = webhookMessagePayload.getPostbackText();
            Map<String, Object> variables = new HashMap<>();
            variables.put("chosen_driver_id",chosenDriverId);
            variables.put("user_selected_ride", true);
            camundaCoreService.completeUserTaskByTaskId(task, variables);
        }
    }
}
