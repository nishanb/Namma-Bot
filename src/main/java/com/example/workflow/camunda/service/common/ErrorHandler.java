package com.example.workflow.camunda.service.common;

import com.example.workflow.models.User;
import com.example.workflow.serviceImpl.CommonMessageService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ErrorHandler implements JavaDelegate {

    private final Logger log = Logger.getLogger(ErrorHandler.class.getName());
    @Autowired
    CommonMessageService commonMessageService;
    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Common ErrorHandler " + this.getClass().getName());
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            if (user != null) {
                commonMessageService.sendErrorMessage(user);
                user.setProcessInstanceId(null);
                user.setSubProcessInstanceId(null);
                userService.updateUser(user.getId(), user);
            } else {
                System.out.println("User is null couldn't send message");
            }
        } catch (Exception e) {
            log.warning("Exception occurred in Common Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
