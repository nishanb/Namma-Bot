package com.example.workflow.camunda.service.ride;

import com.example.workflow.camunda.core.CamundaCoreService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
@Service
public class RideEnded implements JavaDelegate {

    @Autowired
    CamundaCoreService camundaCoreService;
    private final Logger log = Logger.getLogger(com.example.workflow.camunda.service.booking.RideEnded.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("ride.RideEnded: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("ride.RideEnded", true);
            Map<String, Object> variables = new HashMap<>();
            variables.put("ride_ended_message",true);
            String businessKey = execution.getProcessBusinessKey();
            camundaCoreService.createMessageCorrelation(businessKey,"ride_ended_update",variables);
        } catch (Exception e){
            log.warning("RideEnded: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}