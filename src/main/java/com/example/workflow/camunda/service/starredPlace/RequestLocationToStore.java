package com.example.workflow.camunda.service.starredPlace;

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
public class RequestLocationToStore implements JavaDelegate {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    private final Logger log = Logger.getLogger(RequestLocationToStore.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName());
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            messageService.sendTextMessage(new SendMessageRequestDto(execution.getBusinessKey(), templateService.format(MessageTemplate.RIDE_REQUEST_PICKUP_LOCATION, user.getPreferredLanguage())));
        } catch (Exception e) {
            log.warning("Exception occurred in Service Activity : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("starred_place_flow_error", "Error sending message.....");
        }
    }
}
