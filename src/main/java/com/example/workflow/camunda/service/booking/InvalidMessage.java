package com.example.workflow.camunda.service.booking;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class InvalidMessage implements JavaDelegate {

    private final Logger log = Logger.getLogger(InvalidMessage.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("InvalidMessage: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("InvalidMessage", true);
            throw new BpmnError("booking_flow_error","Error sending message.....");
        } catch (Exception e){
            log.warning("InvalidMessage: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
