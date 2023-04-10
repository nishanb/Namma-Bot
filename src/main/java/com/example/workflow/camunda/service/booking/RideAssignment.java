package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;
@Service
public class RideAssignment implements JavaDelegate {

    @Autowired
    NammaYathriService nammaYathriService;

    @Autowired
    MessageService messageService;
    private final Logger log = Logger.getLogger(RideAssignment.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("RideAssignment: execute method is called......");
            String chosenDriverId = (String) execution.getVariable("chosen_driver_id");
            String rideId = (String) execution.getVariable("ride_id");

            //Requesting Namma Yatri backend to assign the ride.
            JsonElement response = nammaYathriService.bookRide(chosenDriverId,rideId);

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


            messageService.sendTextMessage(new SendMessageRequestDto(execution.getBusinessKey(), driverName + " is " + pickupETA +  " minutes away.\n" +
                    "Here's the information about your ride:\n" +
                    "Ride fare: " + "₹"+rideFare+" (Cash/Use any UPI app)\n" +
                    "Vehicle number: "+ vehicleNumber));

            //set relevant variables for future ref
                execution.setVariable("otp", OTP);
                execution.setVariable("eta_to_drop_location", etaToDropLocation);
        } catch (Exception e){
            System.out.println(e.getMessage());
            log.warning("RideAssignment: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
