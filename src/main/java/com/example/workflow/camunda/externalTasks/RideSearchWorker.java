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
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.camunda.bpm.client.variable.value.JsonValue;

@Component
@ExternalTaskSubscription("fetchNearbyRides")
public class RideSearchWorker implements ExternalTaskHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    NammaYathriService nammaYathriService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(RideSearchWorker.class.getName());

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try{
            log.info("RideSearchWorker: execute method called......");
            //Fetching all the variables from the process instance required for execution.
            String destinationLatitude = (String) externalTask.getVariable("destination_latitude");
            String destinationLongitude = (String) externalTask.getVariable("destination_longitude");
            String sourceLatitude = (String) externalTask.getVariable("source_latitude");
            String sourceLongitude = (String) externalTask.getVariable("source_longitude");
            String rideSelectionMode = (String) externalTask.getVariable("ride_selection_mode");
//            TimeUnit.SECONDS.sleep(25);
            //Requesting Namma Yatri backend to find manual rides.
            JsonElement response = nammaYathriService.findNearByRide(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude, rideSelectionMode);

            JsonObject ridesToPersistObj = response.getAsJsonObject().getAsJsonObject("data");
            JsonValue ridesToPersist = ClientValues.jsonValue(new Gson().toJson(ridesToPersistObj));

            VariableMap variables = Variables.createVariables();

            variables.put("rides_to_persist", ridesToPersist);
            variables.put("ride_found",true);
            externalTaskService.complete(externalTask, variables);

        } catch(Exception e){
            System.out.println(e.getMessage());
            log.warning("RideSearchWorker: Exception occured......");
        }
    }
}
