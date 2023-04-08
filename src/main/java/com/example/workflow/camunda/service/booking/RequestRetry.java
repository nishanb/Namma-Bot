package com.example.workflow.camunda.service.booking;

import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.MessageContent;
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

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    private final Logger log = Logger.getLogger(RequestRetry.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{

            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            SendQuickReplyMessageDto retrySelectionMessage = new SendQuickReplyMessageDto();
            retrySelectionMessage.setReceiverContactNumber(user.getPhoneNumber());
            retrySelectionMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", "Retry Search \uD83D\uDD01");
                        put("postbackText", "RETRY_SEARCH");
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", "Cancel Booking ❌");
                        put("postbackText", "CANCEL_BOOKING");
                    }}
            )));

            retrySelectionMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            "No rides found!", String.format("How would you like to proceed with the ride searc`h?")
                    ), options, UUID.randomUUID().toString()));
            messageService.sendQuickReplyMessage(retrySelectionMessage);

            log.info("RequestRetry: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("RequestRetry", true);
        } catch (Exception e){
            log.warning("RequestRetry: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}