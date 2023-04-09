package com.example.workflow.camunda.service.booking;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.config.ConversationWorkflow;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class InitiateRide implements JavaDelegate {

    @Autowired
    CamundaCoreService camundaCoreService;

    @Value("${camunda.process-definition-id.ride-flow}")
    String rideFlowProcessId;
    private final Logger log = Logger.getLogger(com.example.workflow.camunda.service.booking.RideStarted.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            //call gupshup to send message
            log.info("book.InitiateRide: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("InitiateRide", true);
            String businessKey = execution.getProcessBusinessKey();
            camundaCoreService.startProcessInstanceByName(ConversationWorkflow.RIDE_UPDATE.getProcessDefinitionName(), businessKey);
        } catch (Exception e) {
            System.out.println("error message>>>>" + e.getMessage());
            log.warning("book.InitiateRide: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}