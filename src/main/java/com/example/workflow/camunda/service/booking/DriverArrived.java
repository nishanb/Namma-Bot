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
public class DriverArrived implements JavaDelegate {

    private final Logger log = Logger.getLogger(DriverArrived.class.getName());
    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;
    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("--> Executing driver arrived service task <<--");
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElseGet(null);

            String rideOtp = execution.getVariable("otp").toString();

            SendQuickReplyMessageDto driverArrivedMessage = new SendQuickReplyMessageDto();
            driverArrivedMessage.setReceiverContactNumber(user.getPhoneNumber());
            driverArrivedMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(List.of(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.RIDE_RETRY_REQUEST_CANCEL_BOOKING, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.CANCEL_BOOKING.getPostbackText());
                    }}
            )));

            driverArrivedMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.RIDE_BOOKING_TYPE_HEADER, user.getPreferredLanguage()),
                            templateService.format(MessageTemplate.RIDE_DRIVER_ARRIVED_EVENT,
                                    user.getPreferredLanguage(),
                                    new ArrayList<>(Collections.singletonList(rideOtp)))
                    ), options, UUID.randomUUID().toString()));

            messageService.sendQuickReplyMessage(driverArrivedMessage);
        } catch (Exception e) {
            log.warning("DriverArrived: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}