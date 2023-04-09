package com.example.workflow.camunda.service.ride;

import com.example.workflow.camunda.core.CamundaCoreService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.logging.Logger;

@Service
public class RideEnded implements JavaDelegate {

    @Autowired
    CamundaCoreService camundaCoreService;
    private final Logger log = Logger.getLogger(com.example.workflow.camunda.service.booking.RideEnded.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            log.info("<-- Ride Ended  Message-Service method executed -->");

            // driver has ended the ride
            execution.setVariable("ride_status", "driver_arrived");

            camundaCoreService.createMessageCorrelation(execution.getProcessBusinessKey(), "ride_ended_update", new HashMap<>());
        } catch (Exception e) {
            log.warning("RideEnded: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}