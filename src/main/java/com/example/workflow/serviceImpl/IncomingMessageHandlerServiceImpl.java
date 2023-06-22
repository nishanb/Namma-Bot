package com.example.workflow.serviceImpl;

import com.example.workflow.dto.WebhookEventRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.InBoundUserDetails;
import com.example.workflow.models.gupshup.WebhookMessagePayload;
import com.example.workflow.services.MessageWebhookHandlerService;
import com.example.workflow.services.UserService;
import com.example.workflow.services.WorkflowService;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.workflow.utils.Constants.*;

@Service
public class IncomingMessageHandlerServiceImpl implements MessageWebhookHandlerService {
    private static final Logger logger = LoggerFactory.getLogger(IncomingMessageHandlerServiceImpl.class);
    @Autowired
    UserService userService;
    @Autowired
    WorkflowService workflowService;

    @Autowired
    CommonMessageService commonMessageService;

    @Override
    public Boolean handleWebhookEvent(WebhookEventRequestDto webhookEventRequestDto) throws JSONException {
        try {
            if (webhookEventRequestDto.getType().equals(INBOUND_WEBHOOK_EVENTS)) {

                InBoundUserDetails inBoundUserDetails = new InBoundUserDetails();
                inBoundUserDetails.setName(webhookEventRequestDto.getWebhookMessagePayload().getSender().get("name"));
                inBoundUserDetails.setPhone(webhookEventRequestDto.getWebhookMessagePayload().getSender().get("phone"));

                logger.info(">>> Received : Incoming message from " + inBoundUserDetails.getPhone());
                handleIncomingMessage(inBoundUserDetails, webhookEventRequestDto.getWebhookMessagePayload().getType(), webhookEventRequestDto.getWebhookMessagePayload());
            } else if (webhookEventRequestDto.getType().equals(INBOUND_WEBHOOK_USER_EVENTS)) {
                if(webhookEventRequestDto.getWebhookMessagePayload().getType().equals(GUPSHUP_OPTED_IN)) {
                    logger.info(">>> Received : User added event from " + webhookEventRequestDto.getWebhookMessagePayload().getPhone());
                    commonMessageService.sendUserOnboardingMessage(webhookEventRequestDto.getWebhookMessagePayload().getPhone());
                }
                logger.info("<--- Ignoring : Received message webhook ---> " + webhookEventRequestDto.getType());
            }
        } catch (Exception e) {
            logger.error("Failed to process webhook  " + e.getMessage());
        }
        return false;
    }

    public void handleIncomingMessage(InBoundUserDetails inboundUserDetails, String messageType, WebhookMessagePayload webhookMessagePayload) {
        try {
            // TODO : Sync users with Namma Yatri backend
            Optional<User> userDetails = userService.findUserByPhoneNumber(inboundUserDetails.getPhone());
            User user = userDetails.orElseGet(() -> userService.createUser(new User(null, inboundUserDetails.getPhone(), inboundUserDetails.getName())));

            // pass context to workflow service
            workflowService.process(user, messageType, webhookMessagePayload);
        } catch (Exception e) {
            logger.error("Failed to process message " + e.getMessage());
            e.printStackTrace();
        }
    }
}