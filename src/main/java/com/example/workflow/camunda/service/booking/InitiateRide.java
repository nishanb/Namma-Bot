package com.example.workflow.camunda.service.booking;

import com.example.workflow.camunda.core.CamundaCoreService;
import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.models.User;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class InitiateRide implements JavaDelegate {

    private final Logger log = Logger.getLogger(com.example.workflow.camunda.service.booking.RideStarted.class.getName());
    @Autowired
    CamundaCoreService camundaCoreService;
    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);

            // Initiate Ride flow for user (TODO : business key to drivers-number)
            String businessKey = execution.getProcessBusinessKey();
            String subprocessRideFlowId = camundaCoreService.startProcessInstanceByName(ConversationWorkflow.RIDE_UPDATE.getProcessDefinitionName(), businessKey).getProcessInstanceId();

            // Attach sub process instance id to DB ( TODO : Create new backend user table & correlate when actual namma yatri api is provided )
            user.setSubProcessInstanceId(subprocessRideFlowId);
            userService.updateUser(execution.getBusinessKey(), user);
        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}