package com.example.workflow.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

/**
 * Message will be stored in this format
 * {
 * "_id" : uuid_of_db,
 * "templateId" : "const_id",
 * "message" : {
 * "english": "Hello $1 , Good day $2",
 * "kannada": "ನಮಸ್ಕಾರ $1, $2",
 * "hindi": "नमस्ते $1 , $2"
 * }
 * }
 */

@Document
public class Template implements Serializable {
    @Id
    private String id;
    private String templateId;
    private Map<String, String> message;

    public Template(String templateId, Map<String, String> message) {
        this.templateId = templateId;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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