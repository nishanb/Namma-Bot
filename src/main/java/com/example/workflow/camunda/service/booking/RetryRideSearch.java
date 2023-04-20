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
            System.out.println(e.getMessage());
            log.warning("RetryRideSearch: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
