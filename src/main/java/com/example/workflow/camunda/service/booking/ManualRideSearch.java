package com.example.workflow.camunda.service.booking;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.serviceImpl.TemplateServiceImpl;
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
public class ManualRideSearch implements JavaDelegate {

    @Autowired
    TemplateServiceImpl templateService;
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(ManualRideSearch.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("ManualRideSearch: execute method is called......");
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), templateService.format(MessageTemplate.RIDE_FETCH_NEARBY_DRIVERS, user.getPreferredLanguage())));
            execution.setVariable("ride_selection_mode","manual");

        } catch (Exception e){
            System.out.println(e.getMessage());
            log.warning("ManualRideSearch: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
