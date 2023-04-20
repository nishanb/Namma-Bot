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

import java.util.logging.Logger;

public class InvalidMessage implements JavaDelegate {

    private final Logger log = Logger.getLogger(InvalidMessage.class.getName());
    @Autowired
    TemplateService templateService;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElseGet(null);

            messageService.sendTextMessage(new SendMessageRequestDto(
                    templateService.format(MessageTemplate.RIDE_INVALID_MESSAGE, user.getPreferredLanguage()), user.getPhoneNumber()));

            log.info("InvalidMessage: execute method is called......");
        } catch (Exception e) {
            log.warning("InvalidMessage: Exception occurred......" + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
