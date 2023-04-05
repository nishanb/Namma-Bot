package com.example.workflow.camunda.service.booking.userTasks;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.service.UserTask;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.utils.Constants;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ReceiveSourceLocation implements UserTask {

    @Autowired
    CamundaCoreService camundaCoreService;

    @Override
    public void execute(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        if (Objects.equals(messageType, Constants.MESSAGE_TYPE_LOCATION_REPLY)) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("source_latitude", webhookMessagePayload.getPayload().get("latitude"));
            variables.put("source_longitude", webhookMessagePayload.getPayload().get("longitude"));
            camundaCoreService.completeUserTaskByTaskId(task, variables);
        }
    }
}
