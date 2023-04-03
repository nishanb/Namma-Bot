package com.example.workflow.service;

import com.example.workflow.models.User;

import java.util.Optional;

public interface UserService {

    public Optional<User> viewUser(String userId);

    public User createUser(User user);

    public User updateUser(String userId, User user);

    public User updateProcessInstanceIdByUserId(String userId, String processInstanceId);

    public User updateProcessInstanceIdByPhoneNumber(String phoneNumber, String processInstanceId);

    public Optional<User> findUserByProcessInstanceId(String processInstanceId);

    public Optional<User> findUserByPhoneNumber(String phoneNumber);

    public User updateUserLanguageByPhoneNumber(String phoneNumber, String preferredLanguage);
}
