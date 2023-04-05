package com.example.workflow.dto;

import com.example.workflow.models.ListMessage;

import java.util.HashMap;

public class SendListMessageRequestDto {

    private String receiverContactNumber;

    private ListMessage listMessage;


    public SendListMessageRequestDto(String receiverContactNumber, ListMessage listMessage) {
        this.receiverContactNumber = receiverContactNumber;
        this.listMessage = listMessage;
    }

    public SendListMessageRequestDto() {
    }

    public String getReceiverContactNumber() {
        return receiverContactNumber;
    }


    public ListMessage getListMessage() {
        return listMessage;
    }

    public void setListMessage(ListMessage listMessage) {
        this.listMessage = listMessage;
    }
}
