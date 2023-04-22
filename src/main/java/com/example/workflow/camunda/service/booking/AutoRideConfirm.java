package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonObject;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AutoRideConfirm implements JavaDelegate {

    private final Logger log = Logger.getLogger(AutoRideConfirm.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            JsonValue persistedNearbyRides = execution.getVariableTyped("rides_to_persist");
            SpinJsonNode availableRidesData = persistedNearbyRides.getValue();

            String rideId = (String) availableRidesData.prop("ride_id").value();
            SpinJsonNode availableRide = availableRidesData.prop("ride");

            JsonObject rideObj = new JsonObject();
            String driverName = (String) availableRide.prop("driver_name").value();
            rideObj.addProperty("driver_name", driverName);
            String driverRating = (String) availableRide.prop("driver_rating").value();
            rideObj.addProperty("driver_rating", driverRating);
            String rideFare = (String) availableRide.prop("ride_fare").value();
            rideObj.addProperty("ride_fare", rideFare);
            String rideETA = (String) availableRide.prop("eta_to_pickup_location").value();
            rideObj.addProperty("eta_to_pickup_location", rideETA);
            String driverId = (String) availableRide.prop("driver_id").value();
            rideObj.addProperty("driver_id", driverId);
            String vehicleNumber = (String) availableRide.prop("vehicle_number").value();
            rideObj.addProperty("vehicle_number", vehicleNumber);
            String chosenDriverId = driverId;

            JsonObject ridesToPersistObj = new JsonObject();
            ridesToPersistObj.add(chosenDriverId, rideObj);

            JsonValue ridesToPersist = SpinValues.jsonValue(new Gson().toJson(ridesToPersistObj)).create();


            execution.setVariable("ride_id", rideId);
            execution.setVariable("chosen_driver_id", chosenDriverId);
            execution.setVariable("rides_to_persist", ridesToPersist);

        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
