package com.example.workflow.camunda.service.booking;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class RideEnded implements JavaDelegate {

    private final Logger log = Logger.getLogger(RideEnded.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("RideEnded: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("RideEnded", true);
        } catch (Exception e){
            log.warning("RideEnded: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
