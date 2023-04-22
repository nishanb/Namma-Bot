package com.example.workflow.camunda.service.booking;

import camundajar.impl.com.google.gson.*;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.ListMessageDto;
import com.example.workflow.dto.SendListMessageRequestDto;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.GlobalButtons;
import com.example.workflow.models.gupshup.ListMessageItem;
import com.example.workflow.models.gupshup.ListMessageItemOption;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PickupLocation implements JavaDelegate {
    private final Logger log = Logger.getLogger(DestinationLocation.class.getName());
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();
    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;

    @Autowired
    UserService userService;
    @Autowired
    NammaYathriService nammaYathriService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName() + " For Business Key: " + execution.getBusinessKey());

        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            JsonArray usersStarredPlaces = new JsonArray();
            Boolean hasFavouritePlaces = execution.hasVariable("favourite_places");
            if (!hasFavouritePlaces) {
                JsonElement starredPlaces = nammaYathriService.getStarredPlaces(execution.getBusinessKey());

                // Filter users starred places
                for (JsonElement place : starredPlaces.getAsJsonArray()) {
                    JsonObject object = place.getAsJsonObject();
                    if (object.get("phone").getAsString().equals(execution.getBusinessKey())) {
                        usersStarredPlaces.add(object);
                    }
                }
                execution.setVariable("favourite_places", usersStarredPlaces.getAsJsonArray().toString());
                execution.setVariable("favourite_places_size", usersStarredPlaces.getAsJsonArray().size());
            } else {
                if ((Integer) execution.getVariable("favourite_places_size") > 0) {
                    usersStarredPlaces = gson.fromJson(execution.getVariable("favourite_places").toString(), JsonArray.class);
                }
            }

            if (usersStarredPlaces.isEmpty()) {
                messageService.sendTextMessage(new SendMessageRequestDto(execution.getBusinessKey(), templateService.format(MessageTemplate.RIDE_REQUEST_PICKUP_LOCATION,
                        user.getPreferredLanguage())));
            } else {
                ListMessageDto listMessageDto = new ListMessageDto();
                listMessageDto.setTitle("");
                listMessageDto.setBody(templateService.format(MessageTemplate.RIDE_REQUEST_PICKUP_LOCATION,
                        user.getPreferredLanguage()) + templateService.format(MessageTemplate.FAVOURITE_PLACES_OPTIONS_INFO,
                        user.getPreferredLanguage()));

                // Set Global button
                List<GlobalButtons> globalButtonsList = new ArrayList<>(List.of(new GlobalButtons("text", templateService.format(MessageTemplate.OPTION_BUTTON_CHOOSE_FROM_HERE, user.getPreferredLanguage()))));
                listMessageDto.setGlobalButtons(globalButtonsList);

                // List Group
                List<ListMessageItem> listMessageGroup = new ArrayList<>();

                //Other section listGREET_OTHER_SUB_HEADER
                ListMessageItem favouritePlacesOption = new ListMessageItem(templateService.format(MessageTemplate.GREET_OTHER_SUB_HEADER, user.getPreferredLanguage()));
                List<ListMessageItemOption> favouritePlaces = new ArrayList<>();

                //List message options - only one section is being used
                for (JsonElement place : usersStarredPlaces) {
                    String uuid = place.getAsJsonObject().get("id").getAsString();
                    String latitude = place.getAsJsonObject().get("latitude").getAsString();
                    String longitude = place.getAsJsonObject().get("longitude").getAsString();
                    String name = place.getAsJsonObject().get("name").getAsString();
                    favouritePlaces.add(new ListMessageItemOption(name, "", latitude + ":" + longitude));
                }

                favouritePlacesOption.setOptions(favouritePlaces);

                // Add others section to group
                listMessageGroup.add(favouritePlacesOption);

                // set list-message group to main message
                listMessageDto.setItems(listMessageGroup);

                messageService.sendListMessage(new SendListMessageRequestDto(user.getPhoneNumber(), messageService.generateListMessage(listMessageDto)));
            }
        } catch (Exception e) {
            log.warning("Exception occurred in Service Task : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("booking_flow_error", "Error sending message.....");
        }
    }
}
