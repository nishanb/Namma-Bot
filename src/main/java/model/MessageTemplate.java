package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class MessageTemplate implements Serializable {

    private String templateId;
    private String language;
    private String messages;

    @Id
    private String id;

    public String  getTemplateId() { return templateId; }

    public String getMessage() {return messages; }
    public String getLanguage() { return language ;}
}
