package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.services.NammaYathriService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CalculateFare implements JavaDelegate {

    private final Logger log = Logger.getLogger(CalculateFare.class.getName());

    @Autowired
    NammaYathriService nammaYathriService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String destinationLatitude = (String) execution.getVariable("destination_latitude");
            String destinationLongitude = (String) execution.getVariable("destination_longitude");
            String sourceLatitude = (String) execution.getVariable("source_latitude");
            String sourceLongitude = (String) execution.getVariable("source_longitude");

            JsonElement response = nammaYathriService.generateEstimate(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude);
            JsonObject estimationData = response.getAsJsonObject().getAsJsonObject("data");

            execution.setVariable("distance_estimation", estimationData.get("distance").getAsString());
            execution.setVariable("time_estimation", estimationData.get("time").getAsString());
            execution.setVariable("price_est_low", estimationData.get("price_est_low").getAsString());
            execution.setVariable("price_est_high", estimationData.get("price_est_high").getAsString());

            log.info("CalculateFare: execute method is called......");
            //set relevant variables for future ref
        } catch (Exception e) {
            log.warning("CalculateFare: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
