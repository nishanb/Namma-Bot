package com.example.workflow.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document
public class MessageTemplate implements Serializable {

    private String templateId;
    private String language;
    private String messages;

    private String[] variable;


    @Id
    private String id;

    public String getTemplateId() {
        return templateId;
    }

    public String getMessage(String templateId) {
        return messages;
    }

    public String[] getVariable() {
        return variable;
    }

    public String getLanguage() {
        return language;
    }

//   // public void setVariable(String variable){
//         this.variable = variable;
//    }


}
