package com.example.workflow.service;

import com.example.workflow.dto.*;
import com.example.workflow.models.*;

import java.util.List;
import java.util.Map;

public interface MessageService {
    Boolean sendTextMessage(SendMessageRequestDto sendMessageRequestDto) throws Exception;

    Boolean sendListMessage(SendListMessageRequestDto sendListMessageRequestDto) throws Exception;

    Boolean sendAttachment(SendAttachmentMessageDto sendAttachmentMessageDto) throws Exception;

    Boolean sendQuickReplyMessage(SendQuickReplyMessageDto sendQuickReplyMessageDto) throws Exception;

    ListMessage generateListMessage(ListMessageData messageData, List listData, String messageId) throws Exception;

    QuickReplyMessage generateQuickReplyMessage(MessageContent messageContent, List<QuickReplyMessage> listData, String messageId) throws Exception;
}