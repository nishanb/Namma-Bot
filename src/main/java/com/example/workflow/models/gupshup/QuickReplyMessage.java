package com.example.workflow.models.gupshup;

import java.util.List;
import java.util.Map;

public class QuickReplyMessage {
    private String type;
    private String msgid;
    private Map<String, String> content;
    private List<Map<String, String>> options;

    public QuickReplyMessage() {
    }

    public List<Map<String, String>> getOptions() {
        return options;
    }

    public void setOptions(List<Map<String, String>> options) {
        this.options = options;
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

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

}
