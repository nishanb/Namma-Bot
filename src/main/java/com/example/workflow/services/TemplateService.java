package com.example.workflow.services;

import com.example.workflow.models.Template;

import java.util.List;

public interface TemplateService {
    public String format(String templateId, String language, List<String> variables);

    public Template getTemplateById(String templateId);
}
