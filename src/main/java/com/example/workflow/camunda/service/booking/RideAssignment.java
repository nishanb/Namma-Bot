package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.serviceImpl.TemplateServiceImpl;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.UserService;
import com.example.workflow.utils.BackendEventSimulatorHelper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

import static com.example.workflow.utils.Constants.MESSAGE_TYPE_QUICK_REPLY;

@Service
public class RideAssignment implements JavaDelegate {

    private final Logger log = Logger.getLogger(RideAssignment.class.getName());
    @Autowired
    TemplateServiceImpl templateService;
    @Autowired
    NammaYathriService nammaYathriService;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    BackendEventSimulatorHelper backendEventSimulatorHelper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            //call gupshup to send message
            log.info("RideAssignment: execute method is called......");
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            String chosenDriverId = (String) execution.getVariable("chosen_driver_id");
            String rideId = (String) execution.getVariable("ride_id");

            //Requesting Namma Yatri backend to assign the ride.
            JsonElement response = nammaYathriService.bookRide(chosenDriverId, rideId);

            //TODO: Adding validation is API call was success
            JsonObject responseData = response.getAsJsonObject().getAsJsonObject("data");

            String OTP = responseData.get("otp").getAsString();
            String etaToDropLocation = responseData.get("eta_to_drop_location").getAsString();
//            String pickupETA = responseData.get("eta_to_pickup_location").getAsString(); TODO: These details will be used once the ride details API is ready.
            String driverContact = responseData.get("driver_phone").getAsString();

            JsonValue persistedRides = execution.getVariableTyped("rides_to_persist");
            SpinJsonNode persistedRidesJson = persistedRides.getValue();
            SpinJsonNode selectedDriverDetails = persistedRidesJson.prop(chosenDriverId);
            String driverName = (String) selectedDriverDetails.prop("driver_name").value();
            String rideFare = (String) selectedDriverDetails.prop("ride_fare").value();
            String pickupETA = (String) selectedDriverDetails.prop("eta_to_pickup_location").value();
            String vehicleNumber = (String) selectedDriverDetails.prop("vehicle_number").value();

            SendQuickReplyMessageDto rideAssignmentMessage = new SendQuickReplyMessageDto();
            rideAssignmentMessage.setReceiverContactNumber(user.getPhoneNumber());
            rideAssignmentMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(List.of(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.RIDE_RETRY_REQUEST_CANCEL_BOOKING, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.CANCEL_BOOKING.getPostbackText());
                    }}
            )));

            rideAssignmentMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.RIDE_BOOKING_TYPE_HEADER, user.getPreferredLanguage()),
                            templateService.format(MessageTemplate.RIDE_ASSIGNED_INFO,
                                    user.getPreferredLanguage(),
                                    new ArrayList<>(Arrays.asList(driverName, pickupETA, rideFare, vehicleNumber)))
                    ), options, UUID.randomUUID().toString()));

            messageService.sendQuickReplyMessage(rideAssignmentMessage);

            //set relevant variables for future ref
            execution.setVariable("otp", OTP);
            execution.setVariable("eta_to_drop_location", etaToDropLocation);

            //Simulate backend events - 40 seconds , 30 seconds , 2 minutes
            backendEventSimulatorHelper.simulateRideEvents(execution.getBusinessKey(), 10000, 10000, 50000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.warning("RideAssignment: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
