package com.example.workflow.services;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.models.Template;

import java.util.List;

public interface TemplateService {
    public String format(MessageTemplate messageTemplate, String language, List<String> variables);

    public String format(MessageTemplate messageTemplate, String language);


    public Template getTemplateById(String templateId);
}
