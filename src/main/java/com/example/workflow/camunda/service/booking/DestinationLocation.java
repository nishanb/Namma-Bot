package com.example.workflow.camunda.service.booking;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class DestinationLocation implements JavaDelegate {

    private final Logger log = Logger.getLogger(DestinationLocation.class.getName());

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            log.info("DestinationLocation: execute method is called......");
            assert user != null;
            messageService.sendTextMessage(new SendMessageRequestDto(execution.getBusinessKey(), templateService.format(MessageTemplate.RIDE_REQUEST_DESTINATION_LOCATION, user.getPreferredLanguage())));
        } catch (Exception e) {
            log.warning("DestinationLocation: Exception occurred......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
