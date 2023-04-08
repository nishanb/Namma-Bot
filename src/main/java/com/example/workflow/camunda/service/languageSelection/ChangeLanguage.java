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
public class ChangeLanguage implements JavaDelegate {

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(ChangeLanguage.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            assert user != null;

            user.setPreferredLanguage(execution.getVariable("preferred_language").toString().toLowerCase());
            // update instance id to null to initiate new flows
            user.setProcessInstanceId(null);
            userService.updateUser(execution.getBusinessKey(), user);
        } catch (Exception e) {
            log.warning("ChangeLanguage: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}