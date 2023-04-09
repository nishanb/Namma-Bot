package com.example.workflow.camunda.service.booking;

import com.example.workflow.services.MessageService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

public class DriverArrived implements JavaDelegate {

    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;
    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(DriverArrived.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("--> Executing driver arrived service task <<--");
        try {
            //call gupshup to send message
            log.info("DriverArrived: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("DriverArrived", true);
        } catch (Exception e) {
            log.warning("DriverArrived: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
