package com.example.workflow.serviceImpl;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonElement;
import com.example.workflow.dto.*;
import com.example.workflow.helpers.PrepareRequestHelper;
import com.example.workflow.helpers.TransformResponseHelper;
import com.example.workflow.models.gupshup.*;
import com.example.workflow.services.MessageService;
import com.example.workflow.services.TemplateService;
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

    @Autowired
    private WhatsappMsgServiceApiHelper whatsappMsgServiceApiHelper;

    @Autowired
    private PrepareRequestHelper prepareRequestHelper;

    @Autowired
    TemplateService templateService;

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
        //TODO: Add validation to set limit for length of the options as only 3 options are allowed.
        generatedMessage.setOptions(options);

        return generatedMessage;
    }
}