package com.example.workflow.dto;

import java.util.Map;

public class MessageServiceRequestDto {
    private String environment;

    private Map<String, Object> data;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
