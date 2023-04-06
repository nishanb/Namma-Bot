package com.example.workflow.models.gupshup;

public class ListMessageItemOption {
    private String type;
    private String title;
    private String description;

    public ListMessageItemOption() {
    }

    public ListMessageItemOption(String title, String description, String postbackText) {
        this.type = "text";
        if (title.length() > 24) {
            title = title.substring(0, 24);
        }
        this.title = title;

        if (description.length() > 72) {
            description = description.substring(0, 72);
        }
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
        if (title.length() > 24) {
            title = title.substring(0, 24);
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description.length() > 72) {
            description = description.substring(0, 72);
        }
        this.description = description;
    }

    public String getPostbackText() {
        return postbackText;
    }

    public void setPostbackText(String postbackText) {
        this.postbackText = postbackText;
    }
}




