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
public class TryAgainLater implements JavaDelegate {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    private final Logger log = Logger.getLogger(TryAgainLater.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{

            //Sending try again later message
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "Sorry! We are unable to find any rides near you at this moment. \n Please try again later after sometime"));
            //call gupshup to send message
            log.info("TryAgainLater: execute method is called......");
            //set relevant variables for future ref
            execution.setVariable("try_again_later", true);
        } catch (Exception e){
            log.warning("TryAgainLater: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}