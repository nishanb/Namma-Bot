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
public class PickupLocation implements JavaDelegate {

    @Autowired
    MessageService messageService;
    private final Logger log = Logger.getLogger(PickupLocation.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            //call gupshup to send message
            log.info("PickupLocation: execute method is called......");
            //set relevant variables for future ref
            messageService.sendTextMessage(new SendMessageRequestDto(execution.getBusinessKey(), "Please share your pickup address"));

        } catch (Exception e) {
            log.warning("PickupLocation: Exception occured......");
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
