package com.example.workflow.camunda.service.booking;

import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.serviceImpl.TemplateServiceImpl;
import com.example.workflow.services.MessageService;
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
public class RideCancelled implements JavaDelegate {

    private final Logger log = Logger.getLogger(RideCancelled.class.getName());
    @Autowired
    TemplateServiceImpl templateService;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            //Sending ride cancelled message only if the user requested for cancellation
            Boolean isNoResponseClose = execution.hasVariable("NoResponseClose");
            if (!isNoResponseClose) {
                Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
                User user = userSaved.get();

                SendQuickReplyMessageDto rideCancelledMessage = new SendQuickReplyMessageDto();
                rideCancelledMessage.setReceiverContactNumber(user.getPhoneNumber());
                rideCancelledMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

                List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(List.of(
                        new HashMap<>() {{
                            put("type", "text");
                            put("title", templateService.format(MessageTemplate.GREET_MAIN_MENU, user.getPreferredLanguage()));
                            put("postbackText", ConversationWorkflow.MAIN_MENU.getPostbackText());
                        }}
                )));

                rideCancelledMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                        new MessageContent(
                                "",
                                templateService.format(MessageTemplate.RIDE_CANCELLED, user.getPreferredLanguage())
                        ),
                        options,
                        UUID.randomUUID().toString())
                );

                messageService.sendQuickReplyMessage(rideCancelledMessage);
            }
            execution.setVariable("RideCancelled", true);
        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
