package com.example.workflow.services;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.models.Template;

import java.util.List;

public interface TemplateService {
    String format(MessageTemplate messageTemplate, String language, List<String> variables);

    String format(MessageTemplate messageTemplate, String language);


    Template getTemplateById(String templateId);
}
