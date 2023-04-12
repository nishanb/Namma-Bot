package com.example.workflow.camunda.singleMessageTasks;

import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.workflow.utils.Constants.MESSAGE_TYPE_QUICK_REPLY;

@Service
public class ViewPastRide implements SingleMessageTask {
    @Autowired
    TemplateService templateService;

    @Autowired
    MessageService messageService;

    @Override
    public void process(User user) throws Exception {
        SendQuickReplyMessageDto rideSelectionMessage = new SendQuickReplyMessageDto();
        rideSelectionMessage.setReceiverContactNumber(user.getPhoneNumber());
        rideSelectionMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

        List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(List.of(
                new HashMap<>() {{
                    put("type", "text");
                    put("title", templateService.format(MessageTemplate.GREET_MAIN_MENU, user.getPreferredLanguage()));
                    put("postbackText", ConversationWorkflow.MAIN_MENU.getPostbackText());
                }}
        )));

        rideSelectionMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                new MessageContent(
                        "Alright !!",
                        "It appears that you do not have any past ride records on our platform. " +
                                "" +
                                "Click on the main menu and begin a new ride."
                ),
                options,
                UUID.randomUUID().toString())
        );

        messageService.sendQuickReplyMessage(rideSelectionMessage);
    }
}
