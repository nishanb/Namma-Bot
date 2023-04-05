package com.example.workflow.dto;

import com.example.workflow.models.GlobalButtons;
import com.example.workflow.models.ListMessageItem;

import java.util.List;

public class ListMessageDto {
    private String type;
    private String msgId;
    private String title;
    private String body;
    private List<GlobalButtons> globalButtons;
    private List<ListMessageItem> items;

    public ListMessageDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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

    public List<GlobalButtons> getGlobalButtons() {
        return globalButtons;
    }

    public void setGlobalButtons(List<GlobalButtons> globalButtons) {
        this.globalButtons = globalButtons;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<ListMessageItem> items) {
        this.items = items;
    }
}
