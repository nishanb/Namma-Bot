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
public class RideStarted implements JavaDelegate {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    private final Logger log = Logger.getLogger(RideStarted.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("RideStarted: execute method is called......");

        // TODO : add support for emergency & support option
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElseGet(null);

            String etaToDropLocation = execution.getVariable("eta_to_drop_location").toString();

            SendQuickReplyMessageDto rideStartedMessage = new SendQuickReplyMessageDto();
            rideStartedMessage.setReceiverContactNumber(user.getPhoneNumber());
            rideStartedMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.NEED_HELP_BUTTON_INFO, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.NEED_HELP.getPostbackText());
                    }}
            )));

            rideStartedMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.RIDE_BOOKING_TYPE_HEADER, user.getPreferredLanguage()),
                            templateService.format(MessageTemplate.RIDE_STARTED_EVENT,
                                    user.getPreferredLanguage(),
                                    new ArrayList<>(Collections.singletonList(etaToDropLocation)))
                    ), options, UUID.randomUUID().toString()));

            messageService.sendQuickReplyMessage(rideStartedMessage);
            execution.setVariable("ride_status", "ongoing");

        } catch (Exception e) {
            log.warning("RideStarted: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
