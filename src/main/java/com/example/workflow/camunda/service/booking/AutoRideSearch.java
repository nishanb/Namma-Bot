package com.example.workflow.camunda.service.booking;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.serviceImpl.TemplateServiceImpl;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AutoRideSearch implements JavaDelegate {

    private final Logger log = Logger.getLogger(AutoRideSearch.class.getName());

    @Autowired
    TemplateServiceImpl templateService;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    NammaYathriService nammaYathriService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            //call gupshup to send message
            log.info("AutoRideSearch: execute method is called......");
            //Fetching user - Customer requesting for ride book
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), templateService.format(MessageTemplate.RIDE_FETCH_NEARBY_DRIVERS, user.getPreferredLanguage())));
            execution.setVariable("ride_selection_mode", "auto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.warning("AutoRideSearch: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
