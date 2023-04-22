package com.example.workflow.camunda.externalTasks;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@ExternalTaskSubscription("fetchNearbyRides")
public class RideSearchWorker implements ExternalTaskHandler {

    private final Logger log = Logger.getLogger(RideSearchWorker.class.getName());
    @Autowired
    MessageService messageService;
    @Autowired
    NammaYathriService nammaYathriService;
    @Autowired
    UserService userService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        log.info("Executing External Task " + this.getClass().getName());

        try {
            log.info("RideSearchWorker: execute method called......");
            //Fetching all the variables from the process instance required for execution.
            String destinationLatitude = externalTask.getVariable("destination_latitude");
            String destinationLongitude = externalTask.getVariable("destination_longitude");
            String sourceLatitude = externalTask.getVariable("source_latitude");
            String sourceLongitude = externalTask.getVariable("source_longitude");
            String rideSelectionMode = externalTask.getVariable("ride_selection_mode");

            //Requesting Namma Yatri backend to find manual rides.
            JsonElement response = nammaYathriService.findNearByRide(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude, rideSelectionMode);

            JsonObject ridesToPersistObj = response.getAsJsonObject().getAsJsonObject("data");
            JsonValue ridesToPersist = ClientValues.jsonValue(new Gson().toJson(ridesToPersistObj));

            VariableMap variables = Variables.createVariables();

            variables.put("rides_to_persist", ridesToPersist);
            variables.put("ride_found", true);
            externalTaskService.complete(externalTask, variables);

        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
        }
    }
}
