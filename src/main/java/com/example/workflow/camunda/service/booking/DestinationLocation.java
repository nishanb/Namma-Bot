package com.example.workflow.camunda.service.booking;

import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.services.MessageService;
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

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            log.info("DestinationLocation: execute method is called......");
            messageService.sendTextMessage(new SendMessageRequestDto(execution.getBusinessKey(), "Please share your destination address"));
        } catch (Exception e) {
            log.warning("DestinationLocation: Exception occurred......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
