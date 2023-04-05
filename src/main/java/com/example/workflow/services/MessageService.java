package com.example.workflow.services;

import com.example.workflow.dto.*;
import com.example.workflow.models.*;
import com.example.workflow.models.gupshup.ListMessage;
import com.example.workflow.models.gupshup.MessageContent;
import com.example.workflow.models.gupshup.QuickReplyMessage;

import java.util.List;
import java.util.Map;

public interface MessageService {
    Boolean sendTextMessage(SendMessageRequestDto sendMessageRequestDto) throws Exception;

    Boolean sendListMessage(SendListMessageRequestDto sendListMessageRequestDto) throws Exception;

    Boolean sendAttachment(SendAttachmentMessageDto sendAttachmentMessageDto) throws Exception;

    Boolean sendQuickReplyMessage(SendQuickReplyMessageDto sendQuickReplyMessageDto) throws Exception;

    ListMessage generateListMessage(ListMessageDto listMessageDto) throws Exception;

    QuickReplyMessage generateQuickReplyMessage(MessageContent messageContent, List<Map<String, String>> listData, String messageId) throws Exception;

    public void sendGreetingMessage(User user) throws Exception;

    public void sendErrorMessage(User user) throws Exception;
    public void sendOtherOptions(User user) throws Exception;

}