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
public class RequestRetry implements JavaDelegate {

    private final Logger log = Logger.getLogger(RequestRetry.class.getName());
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
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            SendQuickReplyMessageDto retrySelectionMessage = new SendQuickReplyMessageDto();
            retrySelectionMessage.setReceiverContactNumber(user.getPhoneNumber());
            retrySelectionMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.RIDE_RETRY_REQUEST_RETRY_SEARCH, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.RETRY_SEARCH.getPostbackText());
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.RIDE_RETRY_REQUEST_CANCEL_BOOKING, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.CANCEL_BOOKING.getPostbackText());
                    }}
            )));

            retrySelectionMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.RIDE_RETRY_REQUEST_HEADER, user.getPreferredLanguage()), templateService.format(MessageTemplate.RIDE_RETRY_REQUEST_BODY, user.getPreferredLanguage())
                    ), options, UUID.randomUUID().toString()));
            messageService.sendQuickReplyMessage(retrySelectionMessage);
            //set relevant variables for future ref
            execution.setVariable("RequestRetry", true);
        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}