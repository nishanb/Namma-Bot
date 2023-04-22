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
public class RideEndedNotifier implements JavaDelegate {

    private final Logger log = Logger.getLogger(RideEndedNotifier.class.getName());
    @Autowired
    CamundaCoreService camundaCoreService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            // driver has ended the ride
            execution.setVariable("ride_status", "ride_ended");

            camundaCoreService.createMessageCorrelation(execution.getProcessBusinessKey(), "ride_ended_update", new HashMap<>());
        } catch (Exception e) {
            log.warning("Exception occurred in Service Activity : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}