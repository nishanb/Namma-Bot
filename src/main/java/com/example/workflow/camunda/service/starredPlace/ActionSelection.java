package com.example.workflow.camunda.service.starredPlace;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.config.PostBackText;
import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.services.MessageService;
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
public class ActionSelection implements JavaDelegate {

    private final Logger log = Logger.getLogger(ActionSelection.class.getName());
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
            User user = userService.findUserByPhoneNumber(execution.getBusinessKey()).orElse(null);

            SendQuickReplyMessageDto placesOperations = new SendQuickReplyMessageDto();
            placesOperations.setReceiverContactNumber(user.getPhoneNumber());
            placesOperations.setType(MESSAGE_TYPE_QUICK_REPLY);

            List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.STARRED_PLACE_OPTION_ADD, user.getPreferredLanguage()));
                        put("postbackText", PostBackText.STARRED_PLACE_ADD.getPostBackText());
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.STARRED_PLACE_OPTION_DELETE, user.getPreferredLanguage()));
                        put("postbackText", PostBackText.STARRED_PLACE_REMOVE.getPostBackText());
                    }},
                    new HashMap<>() {{
                        put("type", "text");
                        put("title", templateService.format(MessageTemplate.STARRED_PLACE_OPTION_CANCEL, user.getPreferredLanguage()));
                        put("postbackText", PostBackText.STARRED_PLACE_CANCEL.getPostBackText());
                    }}
            )));

            placesOperations.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                    new MessageContent(
                            templateService.format(MessageTemplate.STARRED_PLACE_MAIN_HEADER, user.getPreferredLanguage()),
                            templateService.format(MessageTemplate.STARRED_PLACE_MAIN_BODY,
                                    user.getPreferredLanguage())
                    ), options, UUID.randomUUID().toString()));

            messageService.sendQuickReplyMessage(placesOperations);
        } catch (Exception e) {
            log.warning("Exception occurred in Service Activity : " + this.getClass().getName() + " " + e.getMessage());
            throw new BpmnError("starred_place_flow_error", "Error sending message.....");
        }
    }
}
