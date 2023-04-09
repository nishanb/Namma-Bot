package com.example.workflow.camunda.service.languageSelection;

import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
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
public class CancelNotification implements JavaDelegate {
    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;
    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(CancelNotification.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            // do not show cancel message if it's triggered from delay-signal
            if(execution.hasVariable("NoResponseClose")){
                return;
            }

            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);

            SendQuickReplyMessageDto rideSelectionMessage = new SendQuickReplyMessageDto();
            rideSelectionMessage.setReceiverContactNumber(user.getPhoneNumber());
            rideSelectionMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(List.of(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.GREET_MAIN_MENU, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.MAIN_MENU.getPostbackText());
                    }}
            )));

            rideSelectionMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.LANGUAGE_UPDATE_CONFIRMATION_HEADER, user.getPreferredLanguage()),
                            templateService.format(MessageTemplate.LAST_OPERATION_CANCELED, user.getPreferredLanguage())
                    ),
                    options, UUID.randomUUID().toString())
            );

            messageService.sendQuickReplyMessage(rideSelectionMessage);
            log.info("Cancel flow notify called");
        } catch (Exception e) {
            log.warning("CancelFlow: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}