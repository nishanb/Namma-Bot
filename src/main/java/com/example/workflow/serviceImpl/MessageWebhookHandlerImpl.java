package com.example.workflow.serviceImpl;

import com.example.workflow.dto.WebhookEventRequestDto;
import com.example.workflow.models.InBoundUserDetails;
import com.example.workflow.models.WebhookMessagePayload;
import com.example.workflow.service.MessageService;
import com.example.workflow.service.MessageServiceWebhookHandler;
import com.example.workflow.service.UserService;
import com.example.workflow.models.User;
import com.example.workflow.service.WorkflowService;
import com.example.workflow.utils.Constants;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.workflow.constants.ConversationFlowType;


import java.util.Optional;

import static com.example.workflow.utils.Constants.INBOUND_WEBHOOK_EVENTS;
import static com.example.workflow.utils.Constants.MESSAGE_TYPE_BUTTON_REPLY;

@Service
public class MessageWebhookHandlerImpl implements MessageServiceWebhookHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    WorkflowService workflowService;

    private static final Logger logger = LoggerFactory.getLogger(MessageWebhookHandlerImpl.class);

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
            User user = null;
            user = userDetails.orElseGet(() -> userService.createUser(new User(null, inboundUserDetails.getPhone())));
            user.setName(inboundUserDetails.getName());

            if (user.getProcessInstanceId() == null) {
                // if user has chosen option to initiate flow
                if (messageType.equals(MESSAGE_TYPE_BUTTON_REPLY)) {
                    switch (webhookMessagePayload.getPostbackText()) {
                        case ConversationFlowType.BOOK_RIDE ->
                                workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.BOOK_RIDE));
                        case ConversationFlowType.PREVIOUS_RIDE ->
                                workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.PREVIOUS_RIDE));
                        case ConversationFlowType.MANAGE_PLACES ->
                                workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.MANAGE_PLACES));
                        case ConversationFlowType.SUPPORT ->
                                workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.SUPPORT));
                        case ConversationFlowType.CHANGE_LANGUAGE ->
                                workflowService.initiateWorkflow(user, Constants.CAMUNDA_WORKFLOW_PROCESS_NAME_MAP.get(ConversationFlowType.CHANGE_LANGUAGE));
                        case ConversationFlowType.OTHER -> messageService.sendOtherOptions(user);
                        case default -> messageService.sendErrorMessage(user);
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