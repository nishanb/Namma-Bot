package com.example.workflow.models;

import java.util.List;

public class ListMessageItem {
    private String title;
    private List<ListMessageItemOption> options;

    public ListMessageItem(String title) {
        this.title = title;
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
}
