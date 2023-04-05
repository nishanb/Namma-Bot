package com.example.workflow.repository;

import com.example.workflow.models.Template;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

@EnableMongoRepositories
public interface TemplateRepository extends MongoRepository<Template, String> {
    Optional<Template> findMessagesByTemplateId(String templateId);
}