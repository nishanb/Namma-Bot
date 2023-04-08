package com.example.workflow.camunda.service.common;

import com.example.workflow.models.User;
import com.example.workflow.serviceImpl.CommonMessageService;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ErrorHandler implements JavaDelegate {

    @Autowired
    CommonMessageService commonMessageService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(ErrorHandler.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            log.info("ErrorHandler: execute method is called......");
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            if (user != null) {
                commonMessageService.sendErrorMessage(user);
            }
        } catch (Exception e) {
            log.warning("ErrorHandler: Exception occurred......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
