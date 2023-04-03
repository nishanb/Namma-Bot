package com.example.workflow.serviceImpl;

import com.example.workflow.dto.WebhookEventRequestDto;
import com.example.workflow.models.InBoundUserDetails;
import com.example.workflow.models.WebhookMessagePayload;
import com.example.workflow.service.MessageService;
import com.example.workflow.service.MessageServiceWebhookHandler;
import com.example.workflow.service.UserService;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.workflow.utils.Constants.INBOUND_WEBHOOK_EVENTS;
import static com.example.workflow.utils.Constants.MESSAGE_TYPE_BUTTON_REPLY;

@Service
public class MessageServiceWebhookHandlerImpl implements MessageServiceWebhookHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    private static Logger logger = LoggerFactory.getLogger(MessageServiceWebhookHandlerImpl.class);

    @Override
    public Boolean handleWebhookEvent(WebhookEventRequestDto webhookEventRequestDto) throws JSONException {
        try {
            if (webhookEventRequestDto.getType().equals(INBOUND_WEBHOOK_EVENTS)) {
                System.out.println(webhookEventRequestDto);

                InBoundUserDetails inBoundUserDetails = new InBoundUserDetails();

                inBoundUserDetails.setName(webhookEventRequestDto.getWebhookMessagePayload().getSender().get("name"));
                inBoundUserDetails.setPhone(webhookEventRequestDto.getWebhookMessagePayload().getSender().get("phone"));
                inBoundUserDetails.setCountryCode(webhookEventRequestDto.getWebhookMessagePayload().getSender().get("country_code"));
                inBoundUserDetails.setDialCode(webhookEventRequestDto.getWebhookMessagePayload().getSender().get("dial_code"));

                String messageType = webhookEventRequestDto.getWebhookMessagePayload().getType();

                WebhookMessagePayload webhookMessagePayload = webhookEventRequestDto.getWebhookMessagePayload();


                handleIncomingMessage(inBoundUserDetails, messageType, webhookMessagePayload);

                return true;
            }
            return true;

        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public void handleIncomingMessage(InBoundUserDetails inboundUserDetails, String messageType, WebhookMessagePayload webhookMessagePayload) {
//        try {
//            User userDetails = userService.getUserByPhone(inboundUserDetails.getPhone());
////        if (userDetails && userDetails.getProcessInstanceId()) {
////            System.out.println("process instance selected >>>" + userDetails.getProcessInstanceId());
////
////            workflowService.handleWorkFlow(userDetails.getProcessInstanceId(), inboundUserDetails, messageType, webhookMessagePayload);
////        } else {
//            if (messageType.equals(MESSAGE_TYPE_BUTTON_REPLY)) {
//                switch (webhookMessagePayload.getPostBackText()) {
//                    case "PO" -> {
//                        processInstanceDetails = workflowService.awaitinitiatePOWorkflow(inboundUserDetails);
//                        System.out.println("instance created>>>>" + processInstanceDetails);
//                        workflowService.handleWorkFlow(processInstanceDetails.getId()), inboundUserDetails, null, null);
//                    } case "OTHERS" -> {
//                        messageService.sendKnowMoreMessage(inboundUserDetails);
//                    }
//                    default -> {
//                        workFlowService.handleErrorFlow(inboundUserDetails);
//                    }
//                }
//
//            } else {
//                messageService.sendGreetMessage(inboundUserDetails);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
    }
}
