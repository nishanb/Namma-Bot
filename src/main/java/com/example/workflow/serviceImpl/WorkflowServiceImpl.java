package com.example.workflow.serviceImpl;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.config.BpmnWorkFlow;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.serviceImpl.activityHandlers.RideBookingActivityHandler;
import com.example.workflow.serviceImpl.activityHandlers.LanguageChangeActivityHandler;
import com.example.workflow.services.UserService;
import com.example.workflow.services.WorkflowService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    CamundaCoreService camundaCoreService;

    @Autowired
    UserService userService;

    @Autowired
    LanguageChangeActivityHandler languageChangeActivityHandler;

    @Autowired
    RideBookingActivityHandler rideBookingActivityHandler;


    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Override
    public void handleWorkflow(String processInstanceId, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {
        ActivityInstance activityInstance = camundaCoreService.runtimeService.getActivityInstance(processInstanceId);
        List<ActivityInstance> activeInstances = List.of(activityInstance.getChildActivityInstances());

        if (activeInstances.size() > 0) {
            ActivityInstance currentActivityInstance = activeInstances.get(0);
            Task task = camundaCoreService.getTasksByBusinessKey(user.getPhoneNumber(), activityInstance.getProcessDefinitionId());

            logger.info(String.format("Currently %s is in activity %s on process instance %s", user.getPhoneNumber(), currentActivityInstance.getId(), processInstanceId));

            switch (BpmnWorkFlow.fromProcessDefinitionName(camundaCoreService.getProcessDefinitionNameByProcessInstanceId(activityInstance.getProcessDefinitionId()))) {
                case RIDE_BOOKING -> {
                    rideBookingActivityHandler.handle(task, user, messageType, webhookMessagePayload);
                }
                case UPDATE_LANGUAGE -> {
                    languageChangeActivityHandler.handle(task, user, messageType, webhookMessagePayload);
                }
                case default -> {
                    logger.info(String.format(">>>>>>>> No user task class found for %s  %s<<<<<<<<<", currentActivityInstance.getActivityName(), task.getName()));
                }
            }
        }
    }

    @Override
    public void initiateWorkflow(User user, String processDefinitionId) {
        ProcessInstance processInstance = camundaCoreService.startProcessInstanceByName(BpmnWorkFlow.RIDE_BOOKING.getProcessDefinitionName(), user.getPhoneNumber());
        userService.updateProcessInstanceIdByPhoneNumber(user.getPhoneNumber(), processInstance.getProcessInstanceId());
        logger.info(String.format("Process %s started for user %s with processInstance ID %s", processDefinitionId, user.getPhoneNumber(), processInstance.getProcessInstanceId()));
    }
}