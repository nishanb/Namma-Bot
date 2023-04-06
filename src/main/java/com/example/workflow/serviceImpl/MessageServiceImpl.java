package com.example.workflow.serviceImpl;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.config.ConversationFlowType;
import com.example.workflow.config.TemplateType;
import com.example.workflow.dto.*;
import com.example.workflow.helpers.PrepareRequestHelper;
import com.example.workflow.helpers.TransformResponseHelper;
import com.example.workflow.models.*;
import com.example.workflow.models.gupshup.ListMessage;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.models.gupshup.QuickReplyMessage;
import com.example.workflow.services.MessageService;
import com.example.workflow.utils.Constants;
import com.example.workflow.utils.WhatsappMsgServiceApiHelper;
import okhttp3.RequestBody;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.workflow.utils.Constants.*;

// TODO : make this as factory
@Service
public class MessageServiceImpl implements MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Value("${gupshup-source-contact}")
    private String sourceContactNo;

    @Value("${gupshup-app-name}")
    private String vendorAppName;

    @Value("${gupshup-host}")
    private String gupShupHost;

    @Value("${cb-auth-key}")
    private String cbAuthKey;

    @Autowired
    private WhatsappMsgServiceApiHelper whatsappMsgServiceApiHelper;

    @Autowired
    private PrepareRequestHelper prepareRequestHelper;

    @Override
    public Boolean sendTextMessage(SendMessageRequestDto sendMessageRequestDto) throws Exception {
        try {

            Map<String, String> messagePayload = new HashMap<>();
            messagePayload.put("text", sendMessageRequestDto.getMessage());
            messagePayload.put("type", MESSAGE_TYPE_TEXT);

            String requestString = PrepareRequestHelper.stringifyRequestBody(WHATSAPP_CHANNEL, sourceContactNo, sendMessageRequestDto.getReceiverContactNumber(), PrepareRequestHelper.stringifyJson(messagePayload), vendorAppName);
            String url = String.format("%ssm/api/v1/msg", gupShupHost);

            JsonElement response = whatsappMsgServiceApiHelper.post(url, prepareRequestHelper.prepareRequestBodyForXUrlEncodedType(requestString));
            if (response != null) {
                MessageServiceResponseDto messageServiceResponseDto = TransformResponseHelper.transformMessageServiceResponse(response.toString());
                if (messageServiceResponseDto.getStatus().equals(SUBMITTED)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean sendListMessage(SendListMessageRequestDto sendListMessageRequestDto) throws Exception {
        try {
            String requestString = PrepareRequestHelper.stringifyRequestBody(WHATSAPP_CHANNEL, sourceContactNo, sendListMessageRequestDto.getReceiverContactNumber(), PrepareRequestHelper.stringifyJson(sendListMessageRequestDto.getListMessage()), vendorAppName);

            RequestBody requestBodyTransformed = prepareRequestHelper.prepareRequestBodyForXUrlEncodedType(requestString);

            String url = String.format("%ssm/api/v1/msg", gupShupHost);

            JsonElement response = whatsappMsgServiceApiHelper.post(url, requestBodyTransformed);

            if (response != null) {
                MessageServiceResponseDto messageServiceResponseDto = TransformResponseHelper.transformMessageServiceResponse(response.toString());
                return messageServiceResponseDto.getStatus().equals(SUBMITTED);
            }
            return false;
        } catch (Exception e) {
            logger.error("<<<<< Some error occurred while sending list message...>>>>>" + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean sendAttachment(SendAttachmentMessageDto sendAttachmentMessageDto) throws Exception {
        try {
            String message = "";
            Gson gson = new Gson();
            if (sendAttachmentMessageDto.getAttachmentType().equals(ATTACHMENT_TYPES_IMAGE)) {


                Map<String, String> requestMap = new HashMap<>();

                requestMap.put("type", sendAttachmentMessageDto.getAttachmentType());
                requestMap.put("originalUrl", sendAttachmentMessageDto.getOriginalUrl());
                requestMap.put("previewUrl", sendAttachmentMessageDto.getPreviewUrl());
                requestMap.put("caption", sendAttachmentMessageDto.getCaption());

                message = PrepareRequestHelper.stringifyJson(requestMap);
            } else if (sendAttachmentMessageDto.getAttachmentType().equals(ATTACHMENT_TYPES_FILE)) {
                Map<String, String> requestMap = new HashMap<>();

                requestMap.put("type", sendAttachmentMessageDto.getAttachmentType());
                requestMap.put("url", sendAttachmentMessageDto.getUrl());
                requestMap.put("filename", sendAttachmentMessageDto.getFilename());

                message = PrepareRequestHelper.stringifyJson(requestMap);
            }


            String requestString = PrepareRequestHelper.stringifyRequestBody(WHATSAPP_CHANNEL, sourceContactNo, sendAttachmentMessageDto.getReceiverContactNumber(), message, vendorAppName);

            RequestBody transformedRequestBody = prepareRequestHelper.prepareRequestBodyForXUrlEncodedType(requestString);

            String url = String.format("%ssm/api/v1/msg", gupShupHost);

            JsonElement response = whatsappMsgServiceApiHelper.post(url, transformedRequestBody);

            if (response != null) {
                MessageServiceResponseDto messageServiceResponseDto = TransformResponseHelper.transformMessageServiceResponse(response.toString());
                return messageServiceResponseDto.getStatus().equals(SUBMITTED);
            }
            return false;
        } catch (Exception e) {
            logger.error("<<<<< Some error occurred while sending attachment >>>>>" + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean sendQuickReplyMessage(SendQuickReplyMessageDto sendQuickReplyMessageDto) throws Exception {
        try {
            JSONObject requestBody = new JSONObject();

            requestBody.put("source", sourceContactNo);
            requestBody.put("channel", Constants.WHATSAPP_CHANNEL);
            requestBody.put("destination", sendQuickReplyMessageDto.getReceiverContactNumber());
            requestBody.put("src.name", vendorAppName);
            requestBody.put("message", PrepareRequestHelper.stringifyJson(sendQuickReplyMessageDto.getQuickReplyMessage()));

            String requestString = PrepareRequestHelper.stringifyRequestBody(WHATSAPP_CHANNEL, sourceContactNo, sendQuickReplyMessageDto.getReceiverContactNumber(), PrepareRequestHelper.stringifyJson(sendQuickReplyMessageDto.getQuickReplyMessage()), vendorAppName);


            RequestBody transformedRequestBody = prepareRequestHelper.prepareRequestBodyForXUrlEncodedType(requestString);

            String url = String.format("%ssm/api/v1/msg", gupShupHost);

            JsonElement response = whatsappMsgServiceApiHelper.post(url, transformedRequestBody);

            if (response != null) {
                MessageServiceResponseDto messageServiceResponseDto = TransformResponseHelper.transformMessageServiceResponse(response.toString());
                return messageServiceResponseDto.getStatus().equals(SUBMITTED);
            }
            return false;
        } catch (Exception e) {
            logger.error("<<<<< Some error occurred while sending attachment >>>>>" + e.getMessage());
            return false;
        }
    }

    // Section : Message generators & Helpers
    @Override
    public ListMessage generateListMessage(ListMessageDto listMessageDto) throws Exception {
        ListMessage listMessage = new ListMessage();

        listMessage.setType(MESSAGE_TYPE_LIST);
        listMessage.setTitle(listMessageDto.getTitle());
        if (listMessageDto.getMsgid() != null) {
            listMessage.setMsgid(listMessageDto.getMsgid());
        }
        listMessage.setBody(listMessageDto.getBody());
        listMessage.setGlobalButtons(listMessageDto.getGlobalButtons());
        listMessage.setItems(listMessageDto.getItems());

        return listMessage;
    }

    @Override
    public QuickReplyMessage generateQuickReplyMessage(MessageContent messageContent, List<Map<String, String>> options, String messageId) throws Exception {
        QuickReplyMessage generatedMessage = new QuickReplyMessage();

        Map<String, String> content = new HashMap<>();
        content.put("type", MESSAGE_TYPE_TEXT);
        content.put("header", messageContent.getHeader());
        content.put("text", messageContent.getText());
        content.put("caption", messageContent.getCaption());

        generatedMessage.setType(MESSAGE_TYPE_QUICK_REPLY);
        generatedMessage.setMsgid(messageId);
        generatedMessage.setContent(content);
        generatedMessage.setOptions(options);

        return generatedMessage;
    }

    // Section : Default messages

    // TODO : format message from template service
    @Override
    public void sendGreetingMessage(User user) throws Exception {
        SendQuickReplyMessageDto greetingMessage = new SendQuickReplyMessageDto();

        greetingMessage.setReceiverContactNumber(user.getPhoneNumber());
        greetingMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

//        List<Lis>

        List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                new HashMap<>() {{
                    put("type", "text");
                    put("title", "Book a ride");
                    put("postbackText", ConversationFlowType.BOOK_RIDE);
                }},
                new HashMap<>() {{
                    put("type", "text");
                    put("title", "View past rides");
                    put("postbackText", ConversationFlowType.PREVIOUS_RIDE);
                }},
                new HashMap<>() {{
                    put("type", "text");
                    put("title", "Other");
                    put("postbackText", ConversationFlowType.OTHER);
                }}
        )));

        greetingMessage.setQuickReplyMessage(generateQuickReplyMessage(new MessageContent("Hello " + user.getName() + " !!", "Choose from below options to get started"), options, "dss"));
        sendQuickReplyMessage(greetingMessage);
    }

    @Override
    public void sendErrorMessage(String receiverNumber) throws Exception {
        SendMessageRequestDto sendMessageRequestDto = new SendMessageRequestDto();

        sendMessageRequestDto.setReceiverContactNumber(receiverNumber);
        sendMessageRequestDto.setMessage("Some error occurred...please bear with us");

        this.sendTextMessage(sendMessageRequestDto);
    }

    // TODO : integrate template manager
    @Override
    public void sendOtherOptions(User user) throws Exception {
        SendQuickReplyMessageDto otherOptionsMessage = new SendQuickReplyMessageDto();

        otherOptionsMessage.setReceiverContactNumber(user.getPhoneNumber());
        otherOptionsMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

        List<Map<String, String>> options = new ArrayList<>(new ArrayList<>(Arrays.asList(
                new HashMap<>() {{
                    put("type", "text");
                    put("title", "Manage stared places");
                    put("postbackText", ConversationFlowType.MANAGE_PLACES);
                }},

                new HashMap<>() {{
                    put("type", "text");
                    put("title", "Change language");
                    put("postbackText", ConversationFlowType.CHANGE_LANGUAGE);
                }},
                new HashMap<>() {{
                    put("type", "text");
                    put("title", "Raise support ticket");
                    put("postbackText", ConversationFlowType.SUPPORT);
                }}
        )));

        otherOptionsMessage.setQuickReplyMessage(generateQuickReplyMessage(new MessageContent("Cool", "Choose from below options to get started"), options, "dsx"));
        sendQuickReplyMessage(otherOptionsMessage);
    }

    @Override
    public void sendFeatureNotImplemented(User user) throws Exception {

    }

    // TODO : integrate template manager
    @Override
    public void sendErrorMessage(User user) throws Exception {
        sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "Hello " + user.getName() + " I did not understand that !"));
    }
}