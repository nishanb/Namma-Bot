package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonArray;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.dto.ListMessageDto;
import com.example.workflow.dto.SendListMessageRequestDto;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.GlobalButtons;
import com.example.workflow.models.gupshup.ListMessageItem;
import com.example.workflow.models.gupshup.ListMessageItemOption;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
@Service
public class ManualRideSearch implements JavaDelegate {

    @Autowired
    MessageService messageService;

    @Autowired
    NammaYathriService nammaYathriService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(ManualRideSearch.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("ManualRideSearch: execute method is called......");
            //Fetching user - Customer requesting for ride book
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "Please wait while we are searching for manual rides."));

            //Fetching all the variables from the process instance required for execution.
            String destinationLatitude = (String) execution.getVariable("destination_latitude");
            String destinationLongitude = (String) execution.getVariable("destination_longitude");
            String sourceLatitude = (String) execution.getVariable("source_latitude");
            String sourceLongitude = (String) execution.getVariable("source_longitude");

            //variables to store driver details if user will not select ride in the limited time.
            String chosenDriverId = "";

            //TODO: Using this to forward all the ride details to future execution. To be replaced by the ride details API
            JsonObject ridesToPersistObj = new JsonObject();

            //Requesting Namma Yatri backend to find manual rides.
            JsonElement response = nammaYathriService.findNearByRide(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude, "manual");
            JsonObject manualRidesData = response.getAsJsonObject().getAsJsonObject("data");
            String rideId = manualRidesData.get("ride_id").toString();
            JsonArray availableRides = manualRidesData.getAsJsonArray("rides");


            ListMessageDto listMessageDto = new ListMessageDto();
            // Set List message title and body
            listMessageDto.setTitle("Woo-hoo, we found rides..");
            listMessageDto.setBody("Please select one of the option below. *Within 10 seconds.*");

            // Set Global button
            List<GlobalButtons> globalButtonsList = new ArrayList<>(List.of(new GlobalButtons("text", "Ride Options")));
            listMessageDto.setGlobalButtons(globalButtonsList);

            // List Group
            List<ListMessageItem> listMessageGroup = new ArrayList<>();

            //List message header
            ListMessageItem rideOptions = new ListMessageItem("Choose from below");

            //List message options - only one section is being used
            List<ListMessageItemOption> rideListOptions = new ArrayList<>();
            for (JsonElement ride : availableRides) {
                JsonObject rideObj = ride.getAsJsonObject();
                String driverName = rideObj.get("driver_name").getAsString();
                String driverRating  = rideObj.get("driver_rating").getAsString();
                String rideFare = rideObj.get("ride_fare").getAsString();
                String rideETA = rideObj.get("eta_to_pickup_location").getAsString();
                String driverId = rideObj.get("driver_id").getAsString();
                chosenDriverId = driverId;
                rideListOptions.add(new ListMessageItemOption(driverName+" - "+driverRating+"⭐", "Fare ₹"+rideFare+" <> "+rideETA+"  mins away", driverId));
                ridesToPersistObj.add(driverId,ride);
            }
            rideOptions.setOptions(rideListOptions);
            listMessageGroup.add(rideOptions);
            listMessageDto.setItems(listMessageGroup);

            messageService.sendListMessage(new SendListMessageRequestDto(user.getPhoneNumber(), messageService.generateListMessage(listMessageDto)));

            JsonValue ridesToPersist =  SpinValues.jsonValue(new Gson().toJson(ridesToPersistObj)).create();

            execution.setVariable("ride_id", rideId);
            execution.setVariable("chosen_driver_id", chosenDriverId);
            execution.setVariable("rides_to_persist", ridesToPersist);
            execution.setVariable("user_selected_ride", false);

        } catch (Exception e){
            System.out.println(e.getMessage());
            log.warning("ManualRideSearch: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
