package com.example.workflow.services;

import com.example.workflow.models.User;

import java.util.Optional;

public interface UserService {

    Optional<User> viewUser(String userId);

    User createUser(User user);

    User updateUser(String userId, User user);

    User updateProcessInstanceIdByUserId(String userId, String processInstanceId);

    User updateProcessInstanceIdByPhoneNumber(String phoneNumber, String processInstanceId);

    Optional<User> findUserByProcessInstanceId(String processInstanceId);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

    User updateUserLanguageByPhoneNumber(String phoneNumber, String preferredLanguage);
}
