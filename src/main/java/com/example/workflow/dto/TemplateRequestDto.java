package com.example.workflow.dto;

import java.util.Map;

public class TemplateRequestDto {
    private String templateId;
    private Map<String, String> message;


    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, String> getMessage() {
        return message;
    }

    public void setMessage(Map<String, String> message) {
        this.message = message;
    }

}
