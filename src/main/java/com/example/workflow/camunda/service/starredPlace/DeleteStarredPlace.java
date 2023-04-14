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
public class DeleteStarredPlace implements JavaDelegate {

    @Autowired
    NammaYathriService nammaYathriService;

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    @Autowired
    UserService userService;

    private final Logger log = Logger.getLogger(DeleteStarredPlace.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Executing Service Task " + this.getClass().getName());
        try {
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);

            String placeToDelete = execution.getVariable("place_uuid").toString();
            String placeName = execution.getVariable("name").toString();

            // Store Starred place in backed
            nammaYathriService.deleteStarredPlace(placeToDelete);

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

//            newStarredPlaceAddedMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
//                    new MessageContent(
//                            templateService.format(MessageTemplate.LANGUAGE_UPDATE_CONFIRMATION_HEADER, user.getPreferredLanguage()),
//                            templateService.format(MessageTemplate.LAST_OPERATION_CANCELED, user.getPreferredLanguage())
//                    ),
//                    options, UUID.randomUUID().toString())
//            );

            newStarredPlaceAddedMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            "Cool !!",
                            "We've deleted your starred place"
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
