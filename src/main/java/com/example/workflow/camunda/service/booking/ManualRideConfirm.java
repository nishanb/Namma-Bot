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
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.SpinList;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ManualRideConfirm implements JavaDelegate {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(ManualRideConfirm.class.getName());
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("ManualRideConfirm: execute method is called......");

            //variables to store driver details if user will not select ride in the limited time.
            String chosenDriverId = "";

            //TODO: Using this to forward all the ride details to future execution. To be replaced by the ride details API
            JsonObject ridesToPersistObj = new JsonObject();
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            JsonValue persistedNearbyRides = execution.getVariableTyped("rides_to_persist");
            SpinJsonNode availableRidesData = persistedNearbyRides.getValue();

            String rideId = (String) availableRidesData.prop("ride_id").value();

            SpinList<SpinJsonNode> availableRides = availableRidesData.prop("rides").elements();

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
            for (Iterator<SpinJsonNode> it = availableRides.iterator(); it.hasNext();) {
                JsonObject rideObj = new JsonObject();
                SpinJsonNode ride = it.next();
                String driverName = (String) ride.prop("driver_name").value();
                rideObj.addProperty("driver_name",driverName);
                String driverRating = (String) ride.prop("driver_rating").value();
                rideObj.addProperty("driver_rating",driverRating);
                String rideFare = (String) ride.prop("ride_fare").value();
                rideObj.addProperty("ride_fare",rideFare);
                String rideETA = (String) ride.prop("eta_to_pickup_location").value();
                rideObj.addProperty("eta_to_pickup_location",rideETA);
                String driverId = (String) ride.prop("driver_id").value();
                rideObj.addProperty("driver_id",driverId);
                String vehicleNumber = (String) ride.prop("vehicle_number").value();
                rideObj.addProperty("vehicle_number",vehicleNumber);
                chosenDriverId = driverId;
                rideListOptions.add(new ListMessageItemOption(driverName+" - "+driverRating+"⭐", "Fare ₹"+rideFare+" <> "+rideETA+"  mins away", driverId));
                ridesToPersistObj.add(driverId,rideObj);

            }
            rideOptions.setOptions(rideListOptions);
            listMessageGroup.add(rideOptions);
            listMessageDto.setItems(listMessageGroup);

            messageService.sendListMessage(new SendListMessageRequestDto(user.getPhoneNumber(), messageService.generateListMessage(listMessageDto)));
            JsonValue ridesToPersist =  SpinValues.jsonValue(new Gson().toJson(ridesToPersistObj)).create();

            execution.setVariable("rides_to_persist", ridesToPersist);
            execution.setVariable("ride_id", rideId);
            execution.setVariable("chosen_driver_id",chosenDriverId);
        } catch (Exception e){
            System.out.println(e.getMessage());
            log.warning("ManualRideConfirm: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
