package com.example.workflow.camunda.service.booking;

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
public class RideActivityScrubber implements JavaDelegate {
    private final Logger log = Logger.getLogger(RideActivityScrubber.class.getName());
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Activity_Scrubber is executed");
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);

            // as part of cleanup detach process instance id , sub process instance id from user
            user.setProcessInstanceId(null);
            user.setSubProcessInstanceId(null);
            userService.updateUser(execution.getBusinessKey(), user);

            // Thank you Message
            SendQuickReplyMessageDto thankYouMessage = new SendQuickReplyMessageDto();
            thankYouMessage.setReceiverContactNumber(user.getPhoneNumber());
            thankYouMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(List.of(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.GREET_MAIN_MENU, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.MAIN_MENU.getPostbackText());
                    }}
            )));

            thankYouMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.LANGUAGE_UPDATE_CONFIRMATION_HEADER, user.getPreferredLanguage()),
                            templateService.format(MessageTemplate.RIDE_CLEANUP_THANK_YOU, user.getPreferredLanguage())
                    ),
                    options, UUID.randomUUID().toString())
            );
            messageService.sendQuickReplyMessage(thankYouMessage);
        } catch (Exception e) {
            log.warning("Activity Scrubber: Exception occurred......" + e.getMessage());
            throw new BpmnError("Activity_Scrubber", "Error sending message.....");
        }
    }
}