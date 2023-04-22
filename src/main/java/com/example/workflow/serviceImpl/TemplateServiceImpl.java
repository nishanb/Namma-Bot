package com.example.workflow.serviceImpl;

import com.example.workflow.config.MessageTemplate;
import com.example.workflow.models.Template;
import com.example.workflow.repository.TemplateRepository;
import com.example.workflow.services.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    TemplateRepository templateRepository;

    private static final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

    @Override
    public String format(MessageTemplate messageTemplate, String language, List<String> variables) {
        Template template = getTemplateById(messageTemplate.getTemplateId());

        String formattedMessage = "";
        try {
            if (template != null) {
                formattedMessage = template.getMessage().get(language);
                for (int position = 0; position < variables.size(); position++) {
                    formattedMessage = formattedMessage.replace("$" + (position + 1), variables.get(position));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to format message " + e.getMessage() );
        }
        return formattedMessage;
    }

    @Cacheable(value = "generatedTemplate")
    @Override
    public String format(MessageTemplate messageTemplate, String language) {
        Template template = getTemplateById(messageTemplate.getTemplateId());

        String formattedMessage = "";
        if (template != null) {
            formattedMessage = template.getMessage().get(language);
        }
        return formattedMessage;
    }

    @Override
    public Template getTemplateById(String templateId) {
        return templateRepository.findMessagesByTemplateId(templateId).orElse(null);
    }
}