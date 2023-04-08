package com.example.workflow.serviceImpl;

import com.example.workflow.config.ConversationWorkflow;
import com.example.workflow.config.MessageTemplate;
import com.example.workflow.dto.ListMessageDto;
import com.example.workflow.dto.SendListMessageRequestDto;
import com.example.workflow.dto.SendMessageRequestDto;
import com.example.workflow.dto.SendQuickReplyMessageDto;
import com.example.workflow.models.User;
import com.example.workflow.models.gupshup.GlobalButtons;
import com.example.workflow.models.gupshup.ListMessageItem;
import com.example.workflow.models.gupshup.ListMessageItemOption;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.workflow.utils.Constants.MESSAGE_TYPE_QUICK_REPLY;

@Service
public class CommonMessageService {

    @Autowired
    TemplateServiceImpl templateService;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    public void sendGreetingMessage(User user) throws Exception {
        SendQuickReplyMessageDto greetingMessage = new SendQuickReplyMessageDto();
        greetingMessage.setReceiverContactNumber(user.getPhoneNumber());
        greetingMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

        List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                new HashMap<>() {{
                    put("type", "text");
                    put("title", templateService.format(MessageTemplate.GREET_MAIN_OPTION_BOOK_RIDE, user.getPreferredLanguage()));
                    put("postbackText", ConversationWorkflow.RIDE_BOOKING.getPostbackText());
                }},
                new HashMap<>() {{
                    put("type", "text");
                    put("title", templateService.format(MessageTemplate.GREET_MAIN_OPTION_VIEW_PAST_RIDE, user.getPreferredLanguage()));
                    put("postbackText", ConversationWorkflow.PREVIOUS_RIDE.getPostbackText());
                }},
                new HashMap<>() {{
                    put("type", "text");
                    put("title", templateService.format(MessageTemplate.GREET_MAIN_OPTION_MORE_OPTION, user.getPreferredLanguage()));
                    put("postbackText", ConversationWorkflow.OTHER.getPostbackText());
                }}
        )));

        greetingMessage.setQuickReplyMessage(messageService.generateQuickReplyMessage(
                new MessageContent(
                        templateService.format(MessageTemplate.GREET_MAIN_HEADER, user.getPreferredLanguage(), new ArrayList<>(Collections.singletonList(user.getName()))),
                        templateService.format(MessageTemplate.GREET_MAIN_CONTENT, user.getPreferredLanguage())
                ), options, UUID.randomUUID().toString()));
        messageService.sendQuickReplyMessage(greetingMessage);
    }

    public void sendOtherOptions(User user) throws Exception {

        ListMessageDto listMessageDto = new ListMessageDto();
        listMessageDto.setTitle(templateService.format(MessageTemplate.GREET_OTHER_HEADER, user.getPreferredLanguage()));
        listMessageDto.setBody(templateService.format(MessageTemplate.GREET_OTHER_BODY, user.getPreferredLanguage()));

        // Set Global button
        List<GlobalButtons> globalButtonsList = new ArrayList<>(List.of(new GlobalButtons("text", templateService.format(MessageTemplate.OPTION_BUTTON_CHOOSE_FROM_HERE, user.getPreferredLanguage()))));
        listMessageDto.setGlobalButtons(globalButtonsList);

        // List Group
        List<ListMessageItem> listMessageGroup = new ArrayList<>();

        //Other section listGREET_OTHER_SUB_HEADER
        ListMessageItem otherOptions = new ListMessageItem(templateService.format(MessageTemplate.GREET_OTHER_SUB_HEADER, user.getPreferredLanguage()));
        otherOptions.setOptions(Arrays.asList(
                new ListMessageItemOption(templateService.format(MessageTemplate.GREET_OTHER_OPTION_MANAGE_FAV_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.GREET_OTHER_OPTION_MANAGE_FAV_DESC, user.getPreferredLanguage()), ConversationWorkflow.MANAGE_PLACES.getPostbackText()),
                new ListMessageItemOption(templateService.format(MessageTemplate.GREET_OTHER_OPTION_LANGUAGE_UPDATE_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.GREET_OTHER_OPTION_LANGUAGE_UPDATE_DESC, user.getPreferredLanguage()), ConversationWorkflow.UPDATE_LANGUAGE.getPostbackText()),
                new ListMessageItemOption(templateService.format(MessageTemplate.GREET_OTHER_OPTION_SUPPORT_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.GREET_OTHER_OPTION_SUPPORT_DESC, user.getPreferredLanguage()), ConversationWorkflow.SUPPORT.getPostbackText()),
                new ListMessageItemOption(templateService.format(MessageTemplate.GREET_OTHER_OPTION_FEEDBACK_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.GREET_OTHER_OPTION_FEEDBACK_DESC, user.getPreferredLanguage()), ConversationWorkflow.FEEDBACK.getPostbackText()),
                new ListMessageItemOption(templateService.format(MessageTemplate.GREET_OTHER_OPTION_OPEN_DATA_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.GREET_OTHER_OPTION_OPEN_DATA_DESC, user.getPreferredLanguage()), ConversationWorkflow.OPEN_DATA.getPostbackText()),
                new ListMessageItemOption(templateService.format(MessageTemplate.GREET_OTHER_OPTION_KNOW_MORE_TITLE, user.getPreferredLanguage()), templateService.format(MessageTemplate.GREET_OTHER_OPTION_KNOW_MORE_DESC, user.getPreferredLanguage()), ConversationWorkflow.KNOW_MORE.getPostbackText())
        ));

        // Add others section to group
        listMessageGroup.add(otherOptions);

        // set list-message group to main message
        listMessageDto.setItems(listMessageGroup);

        messageService.sendListMessage(new SendListMessageRequestDto(user.getPhoneNumber(), messageService.generateListMessage(listMessageDto)));
    }

    public void sendFeatureNotImplemented(User user) throws Exception {
        messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "This feature is not supported yet"));
    }

    public void sendErrorMessage(User user) throws Exception {
        messageService.sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), templateService.format(MessageTemplate.BOT_ERROR, user.getPreferredLanguage())));
    }
}