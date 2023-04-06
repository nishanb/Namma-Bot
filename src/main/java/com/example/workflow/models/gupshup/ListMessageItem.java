package com.example.workflow.models.gupshup;

import java.util.List;

public class ListMessageItem {
    private String title;
    private String subtitle;
    private List<ListMessageItemOption> options;

    public ListMessageItem(String title) {
        this.title = title;
    }

    public ListMessageItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ListMessageItemOption> getOptions() {
        return options;
    }

    public void setOptions(List<ListMessageItemOption> options) {
        this.options = options;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
