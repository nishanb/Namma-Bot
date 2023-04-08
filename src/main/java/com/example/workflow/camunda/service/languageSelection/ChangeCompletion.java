package com.example.workflow.camunda.service.languageSelection;

import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.config.PostBackText;
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
public class ChangeCompletion implements JavaDelegate {

    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;
    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(ChangeCompletion.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
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
                            templateService.format(MessageTemplate.LANGUAGE_UPDATE_DONE, user.getPreferredLanguage())
                    ), options, UUID.randomUUID().toString()));

            messageService.sendQuickReplyMessage(rideSelectionMessage);
            log.info("Change completion called");
        } catch (Exception e) {
            log.warning("ChangeCompletion: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}