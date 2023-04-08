package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonArray;
import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.dto.ListMessageDto;
import com.example.workflow.dto.SendListMessageRequestDto;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.GlobalButtons;
import com.example.workflow.models.gupshup.ListMessageItem;
import com.example.workflow.models.gupshup.ListMessageItemOption;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
@Service
public class ManualRideSearch implements JavaDelegate {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(ManualRideSearch.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try{
            //call gupshup to send message
            log.info("ManualRideSearch: execute method is called......");
            Optional<User> userSaved = userService.findUserByPhoneNumber(execution.getBusinessKey());
            User user = userSaved.get();

            messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "Please wait while we are searching for manual rides."));
            execution.setVariable("ride_selection_mode","manual");

        } catch (Exception e){
            System.out.println(e.getMessage());
            log.warning("ManualRideSearch: Exception occured......");
            throw new BpmnError("booking_flow_error","Error sending message.....");
        }
    }
}
