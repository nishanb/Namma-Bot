package com.example.workflow.models;

import java.util.List;
import java.util.Map;

public class QuickReplyMessage {
    private String type;
    private String msgId;
    private Map<String, String> content;

    public List<Map<String, String>> getOptions() {
        return options;
    }

    public void setOptions(List<Map<String, String>> options) {
        this.options = options;
    }

    private List<Map<String, String>> options;

    public QuickReplyMessage() {
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

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

}
