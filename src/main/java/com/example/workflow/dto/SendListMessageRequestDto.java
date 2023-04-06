package com.example.workflow.dto;

import com.example.workflow.models.gupshup.ListMessage;

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
        ListMessage listMessageFormatted = new ListMessage();

        listMessageFormatted.setTitle(listMessage.getTitle());
        listMessageFormatted.setType(listMessage.getType());
        listMessageFormatted.setGlobalButtons(listMessage.getGlobalButtons());
        listMessageFormatted.setBody(listMessage.getBody());
        listMessageFormatted.setItems(listMessage.getItems());
        if (listMessage.getMsgid() != null) {
            listMessageFormatted.setMsgid(listMessage.getMsgid());
        }

        return listMessageFormatted;
    }

    public void setListMessage(ListMessage listMessage) {
        this.listMessage = listMessage;
    }
}
