package com.example.workflow.camunda.service.booking;

import com.example.workflow.config.ConversationFlowType;
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

            rideSelectionMessage.setReceiverContactNumber(execution.getBusinessKey().toString());
            rideSelectionMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", "Assign Auto");
                        put("postbackText", ConversationFlowType.BOOK_RIDE);
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", "Choose Manually");
                        put("postbackText", ConversationFlowType.PREVIOUS_RIDE);
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", "Cancel Booking");
                        put("postbackText", ConversationFlowType.OTHER);
                    }}
            )));

            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            String distanceEstimation = execution.getVariable("distance_estimation").toString();
            String timeEstimation = execution.getVariable("time_estimation").toString();
            String priceEstLow = execution.getVariable("price_est_low").toString();
            String priceEstHigh = execution.getVariable("price_est_high").toString();


//            rideSelectionMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
//                    new MessageContent(
//                            templateService.format(TemplateType.USER_GREET, user.getPreferredLanguage(), new ArrayList<>(Collections.singletonList(user.getName()))),
//                            templateService.format(TemplateType.USER_GREET_DESCRIPTION, user.getPreferredLanguage(), new ArrayList<>())
//                    ), options, UUID.randomUUID().toString()));


            rideSelectionMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            "Choose your ride fare", String.format("This ride will cost you %s RS - %s RS , with estimate travel of %s KM in %s minutes ", priceEstLow, priceEstHigh, distanceEstimation, timeEstimation)
                    ), options, UUID.randomUUID().toString()));


            messageService.sendQuickReplyMessage(rideSelectionMessage);

            log.info("BookingType: execute method is called......");
        } catch (Exception e) {
            log.warning("BookingType: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
