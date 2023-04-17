package com.example.workflow.camunda.service.booking;

import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

import static com.example.workflow.utils.Constants.MESSAGE_TYPE_QUICK_REPLY;

@Service
public class PickupLocation implements JavaDelegate {

    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(PickupLocation.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            SendQuickReplyMessageDto pickupLocationMessage = new SendQuickReplyMessageDto();
            pickupLocationMessage.setReceiverContactNumber(user.getPhoneNumber());
            pickupLocationMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.FAVOURITE_PLACES_BUTTON_INFO, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.FAVOURITE_PLACES.getPostbackText());
                    }}
            )));

            pickupLocationMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            "",
                            templateService.format(MessageTemplate.RIDE_REQUEST_PICKUP_LOCATION,
                                    user.getPreferredLanguage())
                    ), options, UUID.randomUUID().toString()));

            messageService.sendQuickReplyMessage(pickupLocationMessage);
//            messageService.sendTextMessage(new SendMessageRequestDto(execution.getBusinessKey(), templateService.format(MessageTemplate.RIDE_REQUEST_PICKUP_LOCATION, user.getPreferredLanguage())));
            log.info("PickupLocation: execute method is called......");
        } catch (Exception e) {
            log.warning("PickupLocation: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
