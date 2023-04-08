package com.example.workflow.camunda.service.booking;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.config.PostBackText;
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
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);

            SendQuickReplyMessageDto rideSelectionMessage = new SendQuickReplyMessageDto();
            rideSelectionMessage.setReceiverContactNumber(user.getPhoneNumber());
            rideSelectionMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.RIDE_BOOKING_TYPE_OPTION_AUTO, user.getPreferredLanguage()));
                        put("postbackText", PostBackText.AUTO_ASSIGN.getPostBackText());
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.RIDE_BOOKING_TYPE_OPTION_MANUAL, user.getPreferredLanguage()));
                        put("postbackText", PostBackText.CHOOSE_MANUAL.getPostBackText());
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.RIDE_BOOKING_TYPE_OPTION_UPDATE_LOCATION, user.getPreferredLanguage()));
                        put("postbackText", PostBackText.CHANGE_LOCATION.getPostBackText());
                    }}
            )));

            // TODO:  Cancel booking to be handled globally, cannot add here as we have a quick button links limit of 3

            String distanceEstimation = execution.getVariable("distance_estimation").toString().replace("\"", "");
            String timeEstimation = execution.getVariable("time_estimation").toString().replace("\"", "");
            String priceEstLow = execution.getVariable("price_est_low").toString().replace("\"", "");
            String priceEstHigh = execution.getVariable("price_est_high").toString().replace("\"", "");

            rideSelectionMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.RIDE_BOOKING_TYPE_HEADER, user.getPreferredLanguage()),
                            templateService.format(MessageTemplate.RIDE_BOOKING_TYPE_BODY,
                                    user.getPreferredLanguage(),
                                    new ArrayList<>(Arrays.asList(priceEstLow, priceEstHigh, distanceEstimation, timeEstimation)))
                    ), options, UUID.randomUUID().toString()));

            messageService.sendQuickReplyMessage(rideSelectionMessage);

            log.info("BookingType: execute method is called......");
        } catch (Exception e) {
            log.warning("BookingType: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}

