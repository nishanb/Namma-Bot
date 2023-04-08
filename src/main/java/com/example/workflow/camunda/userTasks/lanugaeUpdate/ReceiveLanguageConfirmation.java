package com.example.workflow.camunda.userTasks.lanugaeUpdate;

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
public class ReceiveLanguageConfirmation implements UserTask {
    @Autowired
    CamundaCoreService camundaCoreService;

    @Override
    public void complete(Task task, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        if (Objects.equals(messageType, Constants.MESSAGE_TYPE_BUTTON_REPLY)) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("languageConfirmation", webhookMessagePayload.getPostbackText());
            camundaCoreService.completeUserTaskByTaskId(task, variables);
        }
    }
}
