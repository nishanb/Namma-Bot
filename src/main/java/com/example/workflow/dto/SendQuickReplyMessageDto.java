package com.example.workflow.dto;

import com.example.workflow.models.QuickReplyMessage;

import java.util.List;
import java.util.Map;

public class SendQuickReplyMessageDto {
    private String type;

    private String receiverContactNumber;

    private QuickReplyMessage quickReplyMessage;

    public SendQuickReplyMessageDto() {
    }

    public SendQuickReplyMessageDto(String type, String receiverContactNumber, QuickReplyMessage quickReplyMessage) {
        this.type = type;
        this.receiverContactNumber = receiverContactNumber;
        this.quickReplyMessage = quickReplyMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QuickReplyMessage getQuickReplyMessage() {
        return quickReplyMessage;
    }

    public void setQuickReplyMessage(QuickReplyMessage quickReplyMessage) {
        this.quickReplyMessage = quickReplyMessage;
    }

    public String getReceiverContactNumber() {
        return receiverContactNumber;
    }

    public void setReceiverContactNumber(String receiverContactNumber) {
        this.receiverContactNumber = receiverContactNumber;
    }
}
