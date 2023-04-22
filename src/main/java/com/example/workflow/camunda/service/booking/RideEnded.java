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
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

@Service
public class RideEnded implements JavaDelegate {

    private final Logger log = Logger.getLogger(RideEnded.class.getName());
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElseGet(null);

            // Find ride fare offered by driver
            String chosenDriverId = (String) execution.getVariable("chosen_driver_id");

            JsonValue persistedRides = execution.getVariableTyped("rides_to_persist");
            SpinJsonNode selectedDriverDetails = persistedRides.getValue().prop(chosenDriverId);
            String rideFare = (String) selectedDriverDetails.prop("ride_fare").value();

            // Notify user to make payment
            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(),
                    templateService.format(MessageTemplate.RIDE_ENDED_EVENT, user.getPreferredLanguage(), new ArrayList<>(Collections.singletonList(rideFare)))));

        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
