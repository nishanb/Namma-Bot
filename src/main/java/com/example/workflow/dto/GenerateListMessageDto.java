package com.example.workflow.dto;

import com.example.workflow.models.ListMessage;
import com.example.workflow.models.ListMessageData;

import java.util.List;

public class GenerateListMessageDto {
    private ListMessageData listMessageData;
    private List listData;
    private String messageId;

    public GenerateListMessageDto() {
    }

    public ListMessageData getListMessageData() {
        return listMessageData;
    }

    public void setListMessageData(ListMessageData listMessageData) {
        this.listMessageData = listMessageData;
    }

    public List getListData() {
        return listData;
    }

    public void setListData(List listData) {
        this.listData = listData;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
