package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonArray;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
@Service
public class AutoRideSearch implements JavaDelegate {

    private final Logger log = Logger.getLogger(AutoRideSearch.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    NammaYathriService nammaYathriService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("AutoRideSearch: execute method is called......");

            //Fetching user - Customer requesting for ride book
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "Please wait while we are searching for rides near you. Your ride will be assigned automatically."));
            execution.setVariable("ride_selection_mode","auto");

            //Fetching all the variables from the process instance required for execution.
            String destinationLatitude = (String) execution.getVariable("destination_latitude");
            String destinationLongitude = (String) execution.getVariable("destination_longitude");
            String sourceLatitude = (String) execution.getVariable("source_latitude");
            String sourceLongitude = (String) execution.getVariable("source_longitude");

            JsonElement response = nammaYathriService.findNearByRide(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude, "auto");
            JsonObject autoRidesData = response.getAsJsonObject().getAsJsonObject("data");
            String rideId = autoRidesData.get("ride_id").toString();
            JsonObject availableRides = autoRidesData.getAsJsonObject("ride");
            String chosenDriverId = availableRides.get("driver_id").getAsString();

            JsonObject ridesToPersistObj = new JsonObject();
            ridesToPersistObj.add(chosenDriverId,availableRides);

            JsonValue ridesToPersist =  SpinValues.jsonValue(new Gson().toJson(ridesToPersistObj)).create();


            execution.setVariable("ride_id", rideId);
            execution.setVariable("chosen_driver_id", chosenDriverId);
            execution.setVariable("rides_to_persist", ridesToPersist);
        } catch (Exception e){
            System.out.println(e.getMessage());
            log.warning("AutoRideSearch: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
