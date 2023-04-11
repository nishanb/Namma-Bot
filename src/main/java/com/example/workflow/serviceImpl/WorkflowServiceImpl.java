package com.example.workflow.serviceImpl;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.serviceImpl.activityHandlers.RideBookingActivityHandler;
import com.example.workflow.serviceImpl.activityHandlers.LanguageChangeActivityHandler;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.UserService;
import com.example.workflow.services.WorkflowService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.workflow.utils.Constants.*;


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

    @Autowired
    MessageService messageService;

    @Autowired
    CommonMessageService commonMessageService;


    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Override
    public void handleWorkflow(String processInstanceId, User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {

        Task task = camundaCoreService.getTaskByProcessDefinitionAndBusinessKey(processInstanceId, user.getPhoneNumber());

        ActivityInstance currentActivityInstance = camundaCoreService.getActivityInstance(processInstanceId);
        String processDefinitionId = currentActivityInstance.getProcessDefinitionId();
        String processDefinitionName = camundaCoreService.getProcessDefinitionNameByProcessDefinitionId(processDefinitionId);

        Boolean cancelRequest = this.checkAndProcessGlobalCancelRequest(processInstanceId, user, messageType, webhookMessagePayload, processDefinitionName);

        if (task != null && !cancelRequest) {
            logger.info(String.format("<<<< Currently %s is in activity %s on process instance %s >>>", user.getPhoneNumber(), task.getName(), processInstanceId));

            switch (ConversationWorkflow.fromProcessDefinitionName(processDefinitionName)) {
                case RIDE_BOOKING -> {
                    rideBookingActivityHandler.handle(task, user, messageType, webhookMessagePayload);
                }
                case UPDATE_LANGUAGE -> {
                    languageChangeActivityHandler.handle(task, user, messageType, webhookMessagePayload);
                }
                case MANAGE_PLACES -> {
                    logger.info(">>>>>>>> Invoked Manage places flow");
                }
                case SUPPORT -> {
                    logger.info(">>>>>>>> Invoked Support flow");
                }
                case FEEDBACK -> {
                    logger.info(">>>>>>>> Invoked feedback flow");
                }
                case default -> {
                    logger.info(String.format(">>>>>>>> No user task class found for %s  %s<<<<<<<<<", task.getName(), task.getName()));
                }
            }
        }
    }

    @Override
    public void process(User user, String messageType, WebhookMessagePayload webhookMessagePayload) throws Exception {

        if (user.getProcessInstanceId() == null || user.getProcessInstanceId().isEmpty()) {
            // // Message callback coming from main greet section

            if (messageType.equals(MESSAGE_TYPE_BUTTON_REPLY)) {
                switch (ConversationWorkflow.fromPostBackText(webhookMessagePayload.getPostbackText())) {
                    // Initiate booking task
                    case RIDE_BOOKING -> initiateWorkflow(user, ConversationWorkflow.RIDE_BOOKING);

                    // Initiate previous ride task
                    case PREVIOUS_RIDE -> initiateWorkflow(user, ConversationWorkflow.PREVIOUS_RIDE);

                    // Send main greeting option to choose from
                    case MAIN_MENU -> commonMessageService.sendGreetingMessage(user);

                    // Send others option for customer to choose
                    case OTHER -> commonMessageService.sendOtherOptions(user);

                    // Did not understand button reply
                    case default -> commonMessageService.sendErrorMessage(user);
                }

            }
            // --> Message callback coming from others section
            else if (messageType.equals(MESSAGE_TYPE_LIST_REPLY)) {
                // --> Valid Workflows present in others sections
                List<ConversationWorkflow> allowedWorkFlows = new ArrayList<>(List.of(
                        ConversationWorkflow.MANAGE_PLACES, ConversationWorkflow.SUPPORT, ConversationWorkflow.UPDATE_LANGUAGE,
                        ConversationWorkflow.FEEDBACK, ConversationWorkflow.KNOW_MORE, ConversationWorkflow.OPEN_DATA
                ));

                // --> If postback message is valid invoke the flow
                if (allowedWorkFlows.contains(ConversationWorkflow.fromPostBackText(webhookMessagePayload.getPostbackText()))) {
                    initiateWorkflow(user, ConversationWorkflow.fromPostBackText(webhookMessagePayload.getPostbackText()));
                } else {
                    commonMessageService.sendErrorMessage(user);
                }

            } else {
                // --> user without any process running
                commonMessageService.sendGreetingMessage(user);
            }

        } else {
            // --> process instance already present for user , handel that workflow
            logger.info("process instance selected for user :" + user.getPhoneNumber() + " ->>" + user.getProcessInstanceId());
            handleWorkflow(user.getProcessInstanceId(), user, messageType, webhookMessagePayload);
        }
    }

    private void initiateWorkflow(User user, ConversationWorkflow conversationWorkflow) {
        if (conversationWorkflow.getProcessDefinitionName().isEmpty()) {
            // TODO : handel default texts / single message tasks here
            logger.info("No process found to invoke for the task " + conversationWorkflow.getPostbackText());
            return;
        }
        ProcessInstance processInstance = camundaCoreService.startProcessInstanceByName(conversationWorkflow.getProcessDefinitionName(), user.getPhoneNumber());
        userService.updateProcessInstanceIdByPhoneNumber(user.getPhoneNumber(), processInstance.getProcessInstanceId());
        logger.info(String.format("Process %s started for user %s with processInstance ID %s", conversationWorkflow.getProcessDefinitionName(), user.getPhoneNumber(), processInstance.getProcessInstanceId()));
    }

    private Boolean checkAndProcessGlobalCancelRequest(String processInstanceId, User user, String messageType, WebhookMessagePayload webhookMessagePayload, String processDefinitionName) throws Exception {
        Boolean isCancelRequest = false;
        if(messageType.equals(MESSAGE_TYPE_TEXT)){
            isCancelRequest = true;
             Map<String, String> messageText = webhookMessagePayload.getPayload();
             if(messageText.get("text").equalsIgnoreCase("cancel")){
                this.handleGlobalCancelRequest(user, processDefinitionName);
             }
        }
        return isCancelRequest;
    }

    private void handleGlobalCancelRequest(User user, String processDefinitionName) throws Exception {
        //TODO: All the flows should check if the current execution can be cancelled based on the business logic.
        switch (ConversationWorkflow.fromProcessDefinitionName(processDefinitionName)) {
            case RIDE_BOOKING -> {
                rideBookingActivityHandler.handleCancelRequest(user, user.getPhoneNumber(), processDefinitionName);
            }
            case UPDATE_LANGUAGE -> {
                languageChangeActivityHandler.handleCancelRequest(user, user.getPhoneNumber(), processDefinitionName);
            }
            case MANAGE_PLACES -> {
                logger.info(">>>>>>>> Invoked Manage places cancel flow");
            }
            case SUPPORT -> {
                logger.info(">>>>>>>> Invoked Support cancel flow");
            }
            case FEEDBACK -> {
                logger.info(">>>>>>>> Invoked feedback cancel flow");
            }
            case default -> {
                logger.info(String.format(">>>>>>>> No cancel handler found for process definition %s  %s<<<<<<<<<", processDefinitionName));
            }
        }
    }
}