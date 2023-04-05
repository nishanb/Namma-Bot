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
            String destinationLatitude = execution.getVariable("destination_latitude").toString();
            String destinationLongitude = execution.getVariable("destination_longitude").toString();
            String sourceLatitude = execution.getVariable("source_latitude").toString();
            String sourceLongitude = execution.getVariable("source_longitude").toString();

            JsonElement response = nammaYathriService.generateEstimate(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude);
            JsonObject estimationData = response.getAsJsonObject().getAsJsonObject("data");

            execution.setVariable("distance_estimation", estimationData.get("distance").toString());
            execution.setVariable("time_estimation", estimationData.get("time").toString());
            execution.setVariable("price_est_low", estimationData.get("price_est_low").toString());
            execution.setVariable("price_est_high", estimationData.get("price_est_high").toString());

            log.info("CalculateFare: execute method is called......");
            //set relevant variables for future ref
        } catch (Exception e) {
            log.warning("CalculateFare: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
