package com.example.workflow.serviceImpl;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.camunda.service.booking.PickupLocation;
import com.example.workflow.models.User;
import com.example.workflow.models.WebhookMessagePayload;
import com.example.workflow.services.UserService;
import com.example.workflow.services.WorkflowService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.workflow.constants.ActivityType;

import java.util.List;


@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    CamundaCoreService camundaCoreService;

    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Override
    public void handleWorkflow(String processInstanceId, User user, String messageType, WebhookMessagePayload webhookMessagePayload) {
        ActivityInstance activityInstance = camundaCoreService.runtimeService.getActivityInstance(processInstanceId);
        List<ActivityInstance> activeInstances = List.of(activityInstance.getChildActivityInstances());
        if(activeInstances.size() > 0 ){
            ActivityInstance currentActivityInstance = activeInstances.get(0);
            logger.info(String.format("Currently %s is in activity %s on process instance %s", user.getPhoneNumber(), currentActivityInstance.getId(), processInstanceId));
        }

//        switch (activityInstance.getActivityId()){
//            case ActivityType.RECEIVE_DESTINATION_LOCATION ->
//        }
    }

    @Override
    public void initiateWorkflow(User user, String processDefinitionId) {
        ProcessInstance processInstance = camundaCoreService.startProcessInstance(processDefinitionId, user.getPhoneNumber());
        userService.updateProcessInstanceIdByPhoneNumber(user.getPhoneNumber(), processInstance.getProcessInstanceId());
        logger.info(String.format("Process %s started for user %s with processInstance ID %s", processDefinitionId, user.getPhoneNumber(), processInstance.getProcessInstanceId()));
    }
}
