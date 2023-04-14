package com.example.workflow.camunda.service.starredPlace;

import camundajar.impl.com.google.gson.JsonObject;
import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.ListMessageDto;
import com.example.workflow.dto.SendListMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.GlobalButtons;
import com.example.workflow.models.gupshup.ListMessageItem;
import com.example.workflow.models.gupshup.ListMessageItemOption;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.services.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.Spin;
import org.camunda.spin.SpinList;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.logging.Logger;

@Service
public class SharePlaceToDelete implements JavaDelegate {

    @Autowired
    UserService userService;

    @Autowired
    TemplateService templateService;

    @Autowired
    MessageService messageService;

    @Resource
    private Gson gson;

    private final Logger log = Logger.getLogger(SharePlaceToDelete.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName());
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            JsonArray availablePlaces = gson.fromJson(execution.getVariable("places").toString(), JsonArray.class);

            ListMessageDto listMessageDto = new ListMessageDto();
            listMessageDto.setTitle(templateService.format(MessageTemplate.GREET_OTHER_HEADER, user.getPreferredLanguage()));
            listMessageDto.setBody(templateService.format(MessageTemplate.GREET_OTHER_BODY, user.getPreferredLanguage()));

            // Set Global button
            List<GlobalButtons> globalButtonsList = new ArrayList<>(List.of(new GlobalButtons("text", templateService.format(MessageTemplate.OPTION_BUTTON_CHOOSE_FROM_HERE, user.getPreferredLanguage()))));
            listMessageDto.setGlobalButtons(globalButtonsList);

            // List Group
            List<ListMessageItem> listMessageGroup = new ArrayList<>();

            //Other section listGREET_OTHER_SUB_HEADER
            ListMessageItem placesToDeleteOption = new ListMessageItem(templateService.format(MessageTemplate.GREET_OTHER_SUB_HEADER, user.getPreferredLanguage()));
            List<ListMessageItemOption> placesToDelete = new ArrayList<>();

            //List message options - only one section is being used
            for (JsonElement place : availablePlaces) {
                String uuid = place.getAsJsonObject().get("id").getAsString();
                String latitude = place.getAsJsonObject().get("latitude").getAsString();
                String longitude = place.getAsJsonObject().get("longitude").getAsString();
                String name = place.getAsJsonObject().get("name").getAsString();
                placesToDelete.add(new ListMessageItemOption(name, latitude + " , " + longitude, uuid + ":" + name));
            }

            placesToDeleteOption.setOptions(placesToDelete);

            // Add others section to group
            listMessageGroup.add(placesToDeleteOption);

            // set list-message group to main message
            listMessageDto.setItems(listMessageGroup);

            messageService.sendListMessage(new SendListMessageRequestDto(user.getPhoneNumber(), messageService.generateListMessage(listMessageDto)));

        } catch (Exception e) {
            log.warning("Exception occurred in Service Activity : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("starred_place_flow_error", "Error sending message.....");
        }
    }
}
