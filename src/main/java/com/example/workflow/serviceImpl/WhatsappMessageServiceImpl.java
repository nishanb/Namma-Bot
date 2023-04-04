package com.example.workflow.serviceImpl;

import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.constants.ConversationFlowType;
import com.example.workflow.dto.*;
import com.example.workflow.helpers.PrepareRequestHelper;
import com.example.workflow.helpers.TransformResponseHelper;
import com.example.workflow.models.*;
import com.example.workflow.service.MessageService;
import com.example.workflow.utils.Constants;
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
public class WhatsappMessageServiceImpl implements MessageService {
    private final Logger logger = LoggerFactory.getLogger(WhatsappMessageServiceImpl.class);

    @Value("${gupshup-source-contact}")
    private String sourceContactNo;

    @Value("${gupshup-app-name}")
    private String vendorAppName;

    @Value("${gupshup-api-key}")
    private String vendorApiKey;

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
            // create the msg payload object
            Map<String, String> messagePayload = new HashMap<>();
            messagePayload.put("text", sendMessageRequestDto.getMessage());
            messagePayload.put("type", MESSAGE_TYPE_TEXT);


            String requestString = PrepareRequestHelper.stringifyRequestBody(WHATSAPP_CHANNEL, sourceContactNo, sendMessageRequestDto.getReceiverContactNumber(), PrepareRequestHelper.stringifyJson(messagePayload), vendorAppName);

            Map<String, String> requestHeaders = new HashMap<>();

            requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
            requestHeaders.put("apikey", vendorApiKey);

            RequestBody requestBodyTransformed = prepareRequestHelper.prepareRequestBodyForXUrlEncodedType(requestString);

            String url = String.format("%ssm/api/v1/msg", gupShupHost);

            JsonElement response = whatsappMsgServiceApiHelper.post(url, requestBodyTransformed, requestHeaders);
            if (response != null) {
                MessageServiceResponseDto messageServiceResponseDto = TransformResponseHelper.transformMessageServiceResponse(response.toString());
                if (messageServiceResponseDto.getStatus().equals(SUBMITTED)) {
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Boolean sendListMessage(SendListMessageRequestDto sendListMessageRequestDto) throws Exception {
        try {
            String requestString = PrepareRequestHelper.stringifyRequestBody(WHATSAPP_CHANNEL, sourceContactNo, sendListMessageRequestDto.getReceiverContactNumber(), PrepareRequestHelper.stringifyJson(sendListMessageRequestDto.getListMessage()), vendorAppName);

            Map<String, String> requestHeaders = new HashMap<>();

            requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
            requestHeaders.put("apikey", vendorApiKey);

            RequestBody requestBodyTransformed = prepareRequestHelper.prepareRequestBodyForXUrlEncodedType(requestString);

            String url = String.format("%ssm/api/v1/msg", gupShupHost);

            System.out.println("<<<<<<<<<<<<<< printing all items in message list >>>>>>>>>");

            printAllItems(sendListMessageRequestDto.getListMessage().getItems());

            System.out.println("<<<<<<<<<<<<<< successfully printed all items in message list >>>>>>>>>");

            JsonElement response = whatsappMsgServiceApiHelper.post(url, requestBodyTransformed, requestHeaders);

            if (response != null) {
                MessageServiceResponseDto messageServiceResponseDto = TransformResponseHelper.transformMessageServiceResponse(response.toString());
                if (messageServiceResponseDto.getStatus().equals(SUBMITTED)) {
                    return true;
                }
                return false;
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
            if (sendAttachmentMessageDto.getAttachmentType().equals(ATTACHMENT_TYPES_IMAGE)) {
                Map<String, String> requestMap = new HashMap<>();

                requestMap.put("type", sendAttachmentMessageDto.getAttachmentType());
                requestMap.put("originalUrl", sendAttachmentMessageDto.getAttachmentURL());
                requestMap.put("previewUrl", sendAttachmentMessageDto.getAttachmentURL());
                requestMap.put("caption", sendAttachmentMessageDto.getMessageText());

                message = PrepareRequestHelper.stringifyJson(requestMap);
            } else if (sendAttachmentMessageDto.getAttachmentType().equals(ATTACHMENT_TYPES_FILE)) {
                Map<String, String> requestMap = new HashMap<>();

                requestMap.put("type", sendAttachmentMessageDto.getAttachmentType());
                requestMap.put("url", sendAttachmentMessageDto.getAttachmentURL());
                requestMap.put("filename", sendAttachmentMessageDto.getMessageText());

                message = PrepareRequestHelper.stringifyJson(requestMap);
            }

            String requestString = PrepareRequestHelper.stringifyRequestBody(WHATSAPP_CHANNEL, sourceContactNo, sendAttachmentMessageDto.getReceiverContactNumber(), PrepareRequestHelper.stringifyJson(message), vendorAppName);

            Map<String, String> requestHeaders = new HashMap<>();

            requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
            requestHeaders.put("apikey", vendorApiKey);

            RequestBody transformedRequestBody = prepareRequestHelper.prepareRequestBodyForXUrlEncodedType(requestString);

            String url = String.format("%ssm/api/v1/msg", gupShupHost);

            JsonElement response = whatsappMsgServiceApiHelper.post(url, transformedRequestBody, requestHeaders);

            if (response != null) {
                MessageServiceResponseDto messageServiceResponseDto = TransformResponseHelper.transformMessageServiceResponse(response.toString());
                if (messageServiceResponseDto.getStatus().equals(SUBMITTED)) {
                    return true;
                }
                return false;
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

            Map<String, String> requestHeaders = new HashMap<>();

            requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
            requestHeaders.put("apikey", vendorApiKey);

            RequestBody transformedRequestBody = prepareRequestHelper.prepareRequestBodyForXUrlEncodedType(requestString);

            String url = String.format("%ssm/api/v1/msg", gupShupHost);

            JsonElement response = whatsappMsgServiceApiHelper.post(url, transformedRequestBody, requestHeaders);

            if (response != null) {
                MessageServiceResponseDto messageServiceResponseDto = TransformResponseHelper.transformMessageServiceResponse(response.toString());
                if (messageServiceResponseDto.getStatus().equals(SUBMITTED)) {
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            logger.error("<<<<< Some error occurred while sending attachment >>>>>" + e.getMessage());
            return false;
        }

    }

    @Override
    public ListMessage generateListMessage(ListMessageData messageData, List listData, String messageId) throws Exception {
        ListMessage listMessage = new ListMessage();

        listMessage.setType(MESSAGE_TYPE_LIST);
        listMessage.setMessageId(messageId);
        listMessage.setBody(messageData.getBody());
        listMessage.setGlobalButtonsList(List.of(messageData.getGlobalButtons()));
        listMessage.setItems(listData);

        return listMessage;
    }

    @Override
    public QuickReplyMessage generateQuickReplyMessage(MessageContent messageContent, List<Map<String, String>> listData, String messageId) throws Exception {
        QuickReplyMessage generatedMessage = new QuickReplyMessage();

        Map<String, String> content = new HashMap<>();
        content.put("type", MESSAGE_TYPE_TEXT);
        content.put("header", messageContent.getHeader());
        content.put("text", messageContent.getText());
        content.put("caption", messageContent.getCaption());

        generatedMessage.setType(MESSAGE_TYPE_QUICK_REPLY);
        generatedMessage.setMsgId(messageId);
        generatedMessage.setContent(content);
        generatedMessage.setOptions(listData);

        return generatedMessage;
    }

    public void printAllItems(List<?> items) {
        for (Object item : items) {
            if (item != null) {
                System.out.println(item);
                System.out.println(item.getClass());
            }
        }
    }

    // TODO : format message from template service
    @Override
    public void sendGreetingMessage(User user) throws Exception {
        SendQuickReplyMessageDto greetingMessage = new SendQuickReplyMessageDto();

        greetingMessage.setReceiverContactNumber(user.getPhoneNumber());
        greetingMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

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
    public void sendOtherOptions(User user) throws Exception {
        SendQuickReplyMessageDto greetingMessage = new SendQuickReplyMessageDto();

        greetingMessage.setReceiverContactNumber(user.getPhoneNumber());
        greetingMessage.setType(MESSAGE_TYPE_QUICK_REPLY);

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

        greetingMessage.setQuickReplyMessage(generateQuickReplyMessage(new MessageContent("Cool", "Choose from below options to get started"), options, "dsx"));
        sendQuickReplyMessage(greetingMessage);
    }

    @Override
    public void sendErrorMessage(User user) throws Exception {
        sendTextMessage(new SendMessageRequestDto(user.getPhoneNumber(), "Hello " + user.getName() + " I did not understand that !"));
    }
}