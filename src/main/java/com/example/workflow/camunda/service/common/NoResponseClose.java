package com.example.workflow.camunda.service.common;

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
public class NoResponseClose implements JavaDelegate {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;
    private final Logger log = Logger.getLogger(NoResponseClose.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("NoResponseClose: execute method is called......");
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "I'm sorry, it seems that I haven't received a response from you. Closing the conversation. \n" +
                    "If you have any further questions or concerns, please feel free to reach out to me again in the future. \n" +
                    "\n" +
                    "Thank you for your time and have a great day! \uD83D\uDC4B\uD83D\uDC4B"));
            execution.setVariable("NoResponseClose", true);
        } catch (Exception e){
            log.warning("NoResponseClose: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}