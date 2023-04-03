package com.example.workflow.models;

public class ListMessageData {
    private String title;
    private String body;
    private GlobalButtons globalButtons;

    public ListMessageData() {
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

    public GlobalButtons getGlobalButtons() {
        return globalButtons;
    }

    public void setGlobalButtons(GlobalButtons globalButtons) {
        this.globalButtons = globalButtons;
    }
}
