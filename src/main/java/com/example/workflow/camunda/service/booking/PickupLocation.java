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
public class PickupLocation implements JavaDelegate {

    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(PickupLocation.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            messageService.sendTextMessage(new SendMessageRequestDto(execution.getBusinessKey(), templateService.format(MessageTemplate.RIDE_REQUEST_PICKUP_LOCATION, user.getPreferredLanguage())));
            log.info("PickupLocation: execute method is called......");
        } catch (Exception e) {
            log.warning("PickupLocation: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
