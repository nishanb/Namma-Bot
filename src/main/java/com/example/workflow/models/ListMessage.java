package com.example.workflow.models;

import java.util.List;

public class ListMessage {
    private String type;
    private String messageId;
    private String title;
    private String body;
    private List<GlobalButtons> globalButtonsList;
    private List<?> items;

    public ListMessage() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<GlobalButtons> getGlobalButtonsList() {
        return globalButtonsList;
    }

    public void setGlobalButtonsList(List<GlobalButtons> globalButtonsList) {
        this.globalButtonsList = globalButtonsList;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }
}
