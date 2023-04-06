package com.example.workflow.serviceImpl;

import com.example.workflow.dto.WebhookEventRequestDto;
import com.example.workflow.models.*;
import com.example.workflow.models.gupshup.InBoundUserDetails;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.MessageWebhookHandlerService;
import com.example.workflow.services.UserService;
import com.example.workflow.services.WorkflowService;
import com.example.workflow.utils.Constants;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.workflow.config.ConversationFlowType;


import java.util.Optional;

import static com.example.workflow.utils.Constants.*;

@Service
public class MessageWebhookHandlerImplServiceImpl implements MessageWebhookHandlerService {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    WorkflowService workflowService;

    private static final Logger logger = LoggerFactory.getLogger(MessageWebhookHandlerImplServiceImpl.class);

    // validate webhook is message and handle message
    @Override
    public Boolean handleWebhookEvent(WebhookEventRequestDto webhookEventRequestDto) throws JSONException {
        try {
            if (webhookEventRequestDto.getType().equals(INBOUND_WEBHOOK_EVENTS)) {

                InBoundUserDetails inBoundUserDetails = new InBoundUserDetails();
                inBoundUserDetails.setName(webhookEventRequestDto.getWebhookMessagePayload().getSender().get("name"));
                inBoundUserDetails.setPhone(webhookEventRequestDto.getWebhookMessagePayload().getSender().get("phone"));

                logger.info(">>> Received message from " + inBoundUserDetails.getPhone());
                handleIncomingMessage(inBoundUserDetails, webhookEventRequestDto.getWebhookMessagePayload().getType(), webhookEventRequestDto.getWebhookMessagePayload());
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public void handleIncomingMessage(InBoundUserDetails inboundUserDetails, String messageType, WebhookMessagePayload webhookMessagePayload) {
        try {
            // Collect user info from DB if exists or create new user
            Optional<User> userDetails = userService.findUserByPhoneNumber(inboundUserDetails.getPhone());
            User user;
            user = userDetails.orElseGet(() -> userService.createUser(new User(null, inboundUserDetails.getPhone())));
            user.setName(inboundUserDetails.getName());

            if (user.getProcessInstanceId() == null) {
                // // Message callback coming from main section
                if (messageType.equals(MESSAGE_TYPE_BUTTON_REPLY)) {
                    switch (webhookMessagePayload.getPostbackText()) {
                        case ConversationFlowType.BOOK_RIDE ->
                                workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.BOOK_RIDE));
                        case ConversationFlowType.PREVIOUS_RIDE -> messageService.sendFeatureNotImplemented(user);
                        //workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.PREVIOUS_RIDE));
                        case ConversationFlowType.OTHER -> messageService.sendOtherOptions(user);
                        case default -> messageService.sendErrorMessage(user.getPhoneNumber());
                    }

                } // Message callback coming from others section
                else if (messageType.equals(MESSAGE_TYPE_LIST_REPLY)) {
                    switch (webhookMessagePayload.getPostbackText()) {
                        case ConversationFlowType.MANAGE_PLACES ->
                                workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.MANAGE_PLACES));
                        case ConversationFlowType.SUPPORT ->
                            //workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.SUPPORT));
                                messageService.sendFeatureNotImplemented(user);
                        case ConversationFlowType.CHANGE_LANGUAGE ->
                            //workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.CHANGE_LANGUAGE));
                                messageService.sendFeatureNotImplemented(user);
                        case ConversationFlowType.FEEDBACK ->
                            //workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.FEEDBACK));
                                messageService.sendFeatureNotImplemented(user);
                        case ConversationFlowType.KNOW_MORE ->
                            //workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.KNOW_MORE));
                                messageService.sendFeatureNotImplemented(user);
                        case ConversationFlowType.FAQ ->
                            //workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.FAQ));
                                messageService.sendFeatureNotImplemented(user);
                        case default -> messageService.sendErrorMessage(user.getPhoneNumber());
                    }
                } else {
                    // user without any process running
                    messageService.sendGreetingMessage(user);
                }

            } else {
                logger.info("process instance selected for user :" + user.getPhoneNumber() + " ->>" + user.getProcessInstanceId());
                workflowService.handleWorkflow(user.getProcessInstanceId(), user, messageType, webhookMessagePayload);
            }
        } catch (Exception e) {
            logger.error("Failed to process message " + e.getMessage());
        }
    }
}