package com.example.workflow.camunda.service.languageSelection;

import com.example.workflow.models.User;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CancelFlow implements JavaDelegate {

    private final Logger log = Logger.getLogger(CancelFlow.class.getName());
    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            assert user != null;

            // as part of cancellation detach process instance id from user
            user.setProcessInstanceId(null);
            user.setSubProcessInstanceId(null);
            userService.updateUser(execution.getBusinessKey(), user);
        } catch (Exception e) {
            log.warning("Exception occurred in Service Activity : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}