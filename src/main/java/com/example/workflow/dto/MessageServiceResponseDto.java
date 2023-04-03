package com.example.workflow.dto;

import java.util.Map;

public class MessageServiceResponseDto {
    private String status;
    private String messageId;
    private Map<String, String> data;

    public MessageServiceResponseDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
