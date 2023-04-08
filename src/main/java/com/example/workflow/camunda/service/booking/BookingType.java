package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.config.ConversationFlowType;
import com.example.workflow.dto.ListMessageDto;
import com.example.workflow.dto.SendListMessageRequestDto;
import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.GlobalButtons;
import com.example.workflow.models.gupshup.ListMessageItem;
import com.example.workflow.models.gupshup.ListMessageItemOption;
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
public class BookingType implements JavaDelegate {

    private final Logger log = Logger.getLogger(BookingType.class.getName());

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {

            SendQuickReplyMessageDto rideSelectionMessage = new SendQuickReplyMessageDto();

            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();
            rideSelectionMessage.setReceiverContactNumber(user.getPhoneNumber());
            rideSelectionMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", "Auto Assign");
                        put("postbackText", "AUTO_ASSIGN");
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", "Choose Manually");
                        put("postbackText", "CHOOSE_MANUAL");
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", "Change Location");
                        put("postbackText", "CHANGE_LOCATION");
                    }}
//                    new HashMap<>() {{
//                        put("type", "text");
//                        put("title", "Cancel Booking");
//                        put("postbackText", "CANCEL_BOOKING");
//                    }}  // Cancel booking to be handled globally, cannot add here as we have a quick button links limit of 10
            )));

            String distanceEstimation = (String) execution.getVariable("distance_estimation");
            String timeEstimation = (String) execution.getVariable("time_estimation");
            String priceEstLow = (String) execution.getVariable("price_est_low");
            String priceEstHigh = (String) execution.getVariable("price_est_high");

            rideSelectionMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            "Choose ride", String.format("This ride will cost you ₹%s - ₹%s, with estimate travel of %s KM in %s minutes \n Kindly choose one of the options below that suits your needs.", priceEstLow, priceEstHigh, distanceEstimation, timeEstimation)
                    ), options, UUID.randomUUID().toString()));


            messageService.sendQuickReplyMessage(rideSelectionMessage);

            log.info("BookingType: execute method is called......");
        } catch (Exception e) {
            log.warning("BookingType: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
