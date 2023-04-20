package com.example.workflow.dto;

public class SendMessageRequestDto {
    private String receiverContactNumber;
    private String message;

    public SendMessageRequestDto() {
    }

    public SendMessageRequestDto(String receiverContactNumber, String message) {
        this.receiverContactNumber = receiverContactNumber;
        this.message = message;
    }

    public String getReceiverContactNumber() {
        return receiverContactNumber;
    }

    public void setReceiverContactNumber(String receiverContactNumber) {
        this.receiverContactNumber = receiverContactNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
