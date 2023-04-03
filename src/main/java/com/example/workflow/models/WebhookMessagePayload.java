package com.example.workflow.models;

import java.util.Map;

public class WebhookMessagePayload {
    private String id;
    private String source;
    private String type;
    /**
     * payload is an object for which the keys vary based on the 'type' of the message
     * type -> text, payload: {"text": String}
     * type -> location, payload: {"longitude": String, "latitude": String}
     */
    private Map<String, String> payload;
    private Map<String, String> sender;

    private String postBackText;

    public WebhookMessagePayload() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    public Map<String, String> getSender() {
        return sender;
    }

    public void setSender(Map<String, String> sender) {
        this.sender = sender;
    }

    public String getPostBackText() {
        return postBackText;
    }

    public void setPostBackText(String postBackText) {
        this.postBackText = postBackText;
    }
}
