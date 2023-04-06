package com.example.workflow.models.gupshup;

import java.util.List;

public class ListMessage {
    private String type;
    private String msgid;
    private String title;
    private String body;
    private List<GlobalButtons> globalButtons;
    private List<ListMessageItem> items;

    public ListMessage() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.length() > 30) {
            title = title.substring(0, 30);
        }

        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body.length() > 1024) {
            body = body.substring(0, 1024);
        }
        this.body = body;
    }

    public List<GlobalButtons> getGlobalButtons() {
        return globalButtons;
    }

    public void setGlobalButtons(List<GlobalButtons> globalButtons) {
        this.globalButtons = globalButtons;
    }

    public List<ListMessageItem> getItems() {
        return items;
    }

    public void setItems(List<ListMessageItem> items) {
        this.items = items;
    }
}
