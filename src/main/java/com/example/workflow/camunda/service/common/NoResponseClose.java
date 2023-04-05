package com.example.workflow.camunda.service.common;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class NoResponseClose implements JavaDelegate {

    private final Logger log = Logger.getLogger(NoResponseClose.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("NoResponseClose: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("NoResponseClose", true);
        } catch (Exception e){
            log.warning("NoResponseClose: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}