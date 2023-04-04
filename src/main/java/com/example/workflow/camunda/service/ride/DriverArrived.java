package com.example.workflow.camunda.service.ride;
import com.example.workflow.camunda.core.CamundaCoreService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DriverArrived implements JavaDelegate {

    @Autowired
    CamundaCoreService camundaCoreService;

    private final Logger log = Logger.getLogger(com.example.workflow.camunda.service.booking.DriverArrived.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("ride.DriverArrived: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("DriverArrived", true);

            Map<String, Object> variables = new HashMap<>();
            variables.put("driver_arrived_message",true);
            String businessKey = execution.getProcessBusinessKey();
            camundaCoreService.createMessageCorrelation(businessKey,"driver_arrived_update",variables);
        } catch (Exception e){
            log.warning("ride.DriverArrived: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
