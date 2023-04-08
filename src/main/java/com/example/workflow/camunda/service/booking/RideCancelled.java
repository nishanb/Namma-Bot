package com.example.workflow.camunda.service.booking;

import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
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
public class RideCancelled implements JavaDelegate {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    private final Logger log = Logger.getLogger(RideCancelled.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{

            //Sending ride cancelled message only if the user requested for cancellation
            Boolean isNoResponseClose = execution.hasVariable("NoResponseClose");
            if(!isNoResponseClose){
                Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
                User user = userSaved.get();

                messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "Sorry to see you go \uD83D\uDE22. Your ride has been cancelled."));
            }
            //call gupshup to send message
            log.info("RideCancelled: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("RideCancelled", true);
        } catch (Exception e){
            log.warning("RideCancelled: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
