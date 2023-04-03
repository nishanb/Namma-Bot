package com.example.workflow.serviceImpl;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.dto.WebhookEventRequestDto;
import com.example.workflow.models.InBoundUserDetails;
import com.example.workflow.models.User;
import com.example.workflow.models.WebhookMessagePayload;
import com.example.workflow.service.WorkflowService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    CamundaCoreService camundaCoreService;

    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Override
    public void handleWorkflow(String processInstanceId, User user, String messageType, WebhookMessagePayload webhookMessagePayload) {
        camundaCoreService.runtimeService.getActivityInstance(processInstanceId);
        // Based on activity complete user tasks
    }

    @Override
    public void initiateWorkflow(User user, String processDefinitionId) {
        ProcessInstance processInstance = camundaCoreService.startProcessInstance(processDefinitionId, user.getPhoneNumber());
        user.setProcessInstanceId(processInstance.getRootProcessInstanceId());
        logger.info(String.format("Process %s started for user %s with processInstance ID %s", processDefinitionId, user.getPhoneNumber(), processInstance.getProcessInstanceId()));
    }
}
