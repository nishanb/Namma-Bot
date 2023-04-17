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
public class DestinationLocation implements JavaDelegate {

    private final Logger log = Logger.getLogger(DestinationLocation.class.getName());

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            log.info("DestinationLocation: execute method is called......");
            assert user != null;

            SendQuickReplyMessageDto destinationLocationMessage = new SendQuickReplyMessageDto();
            destinationLocationMessage.setReceiverContactNumber(user.getPhoneNumber());
            destinationLocationMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.FAVOURITE_PLACES_BUTTON_INFO, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.FAVOURITE_PLACES.getPostbackText());
                    }}
            )));

            destinationLocationMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            "",
                            templateService.format(MessageTemplate.RIDE_REQUEST_DESTINATION_LOCATION,
                                    user.getPreferredLanguage())
                    ), options, UUID.randomUUID().toString()));

            messageService.sendQuickReplyMessage(destinationLocationMessage);
        } catch (Exception e) {
            log.warning("DestinationLocation: Exception occurred......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
