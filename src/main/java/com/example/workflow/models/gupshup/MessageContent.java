package com.example.workflow.models.gupshup;

public class MessageContent {
    private String header;
    private String text;

    private String caption = null;

    public MessageContent(String header, String text) {
        if (header.length() > 20) {
            header = header.substring(0, 20);
        }
        this.header = header;
        this.text = text;
    }

    public MessageContent(String header, String text, String caption) {
        if (header.length() > 20) {
            header = header.substring(0, 20);
        }
        this.header = header;
        this.text = text;
        this.caption = caption;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCaption() {
        if (caption == null) {
            return "";
        }
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
