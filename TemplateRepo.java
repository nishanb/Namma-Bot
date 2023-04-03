package com.example.workflow.repository;


import com.example.workflow.model.MessageTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;


@EnableMongoRepositories
public interface TemplateRepo extends MongoRepository<MessageTemplate, String>  {
    Optional<MessageTemplate> findMessagesById(String templateId);
}
