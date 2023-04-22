package com.example.workflow.camunda.service.booking;

import com.example.workflow.services.MessageService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class RetryRideSearch implements JavaDelegate {

    private final Logger log = Logger.getLogger(RetryRideSearch.class.getName());
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            Boolean hasRetryAttempts = execution.hasVariable("retry_attempts");
            Integer retryAttempts = 1;
            if (hasRetryAttempts) {
                retryAttempts = (Integer) execution.getVariable("retry_attempts");
                retryAttempts += 1;
            }
            execution.setVariable("retry_attempts", retryAttempts);
            execution.setVariable("RetryRideSearch", true);
        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
