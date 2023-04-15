package com.example.workflow.camunda.service.starredPlace;

import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.NammaYathriService;
import com.example.workflow.services.TemplateService;
import com.example.workflow.services.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

import static com.example.workflow.utils.Constants.MESSAGE_TYPE_QUICK_REPLY;

@Service
public class StoreStarredPlace implements JavaDelegate {

    private final Logger log = Logger.getLogger(StoreStarredPlace.class.getName());
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    TemplateService templateService;
    @Autowired
    NammaYathriService nammaYathriService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service" + this.getClass().getName());
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);
            assert user != null;

            // collect api variables present in workflow
            String latitude = execution.getVariable("latitude").toString();
            String longitude = execution.getVariable("longitude").toString();
            String name = execution.getVariable("name").toString();

            // Store Starred place in backed
            nammaYathriService.createStarredPlace(latitude, longitude, name, user.getPhoneNumber());

            // Notify place added and present with main menu
            SendQuickReplyMessageDto newStarredPlaceAddedMessage = new SendQuickReplyMessageDto();
            newStarredPlaceAddedMessage.setReceiverContactNumber(user.getPhoneNumber());
            newStarredPlaceAddedMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(List.of(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.GREET_MAIN_MENU, user.getPreferredLanguage()));
                        put("postbackText", ConversationWorkflow.MAIN_MENU.getPostbackText());
                    }}
            )));

            newStarredPlaceAddedMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.STARRED_PLACE_ADD_LOC_ADDED_HEADER, user.getPreferredLanguage()),
                            templateService.format(MessageTemplate.STARRED_PLACE_ADD_LOC_ADDED_BODY, user.getPreferredLanguage(), new ArrayList<>(Collections.singletonList(name)))
                    ),
                    options, UUID.randomUUID().toString())
            );
            messageService.sendQuickReplyMessage(newStarredPlaceAddedMessage);
        } catch (Exception e) {
            log.warning("Exception occurred in Service Activity : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("starred_place_flow_error", "Error sending message.....");
        }
    }
}
