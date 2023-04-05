package com.example.workflow.models;

public class ListMessageItemOption {
    private String type;
    private String title;
    private String description;

    public ListMessageItemOption(String title, String description, String postbackText) {
        this.type = "text";
        this.title = title;
        this.description = description;
        this.postbackText = postbackText;
    }

    private String postbackText;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostbackText() {
        return postbackText;
    }

    public void setPostbackText(String postbackText) {
        this.postbackText = postbackText;
    }
}




