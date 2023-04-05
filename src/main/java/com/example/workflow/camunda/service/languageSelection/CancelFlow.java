package com.example.workflow.camunda.service.languageSelection;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class CancelFlow implements JavaDelegate {

    private final Logger log = Logger.getLogger(CancelFlow.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("CancelFlow: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("CancelFlow", true);
        } catch (Exception e){
            log.warning("CancelFlow: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}