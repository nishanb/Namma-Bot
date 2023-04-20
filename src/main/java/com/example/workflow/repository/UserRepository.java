package com.example.workflow.repository;


import com.example.workflow.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByProcessInstanceId(String processInstanceId);

    Optional<User> findUserByPhoneNumber(String phoneNumber);
}
