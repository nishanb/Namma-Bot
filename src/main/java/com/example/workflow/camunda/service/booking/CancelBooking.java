package com.example.workflow.camunda.service.booking;

import com.example.workflow.models.User;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CancelBooking implements JavaDelegate {

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(CancelBooking.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            //Updating user process instance by empty string
            userService.updateProcessInstanceIdByUserId(user.getId(), null);

            //call gupshup to send message
            log.info("CancelBooking: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("CancelBooking", true);
        } catch (Exception e) {
            log.warning("CancelBooking: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
