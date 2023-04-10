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

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

@Service
public class RideStarted implements JavaDelegate {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    private final Logger log = Logger.getLogger(RideStarted.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("RideStarted: execute method is called......");

        // TODO : add support for emergency & support option
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElseGet(null);

            String etaToDropLocation = execution.getVariable("eta_to_drop_location").toString();
            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(),
                    templateService.format(MessageTemplate.RIDE_STARTED_EVENT, user.getPreferredLanguage(), new ArrayList<>(Collections.singletonList(etaToDropLocation)))));

        } catch (Exception e) {
            log.warning("RideStarted: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
