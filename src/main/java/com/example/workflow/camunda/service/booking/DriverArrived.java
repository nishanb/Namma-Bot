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
public class DriverArrived implements JavaDelegate {

    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;
    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(DriverArrived.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("--> Executing driver arrived service task <<--");
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElseGet(null);

            String rideOtp = execution.getVariable("otp").toString();
            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(),
                    templateService.format(MessageTemplate.RIDE_DRIVER_ARRIVED_EVENT, user.getPreferredLanguage(), new ArrayList<>(Collections.singletonList(rideOtp)))));
        } catch (Exception e) {
            log.warning("DriverArrived: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}