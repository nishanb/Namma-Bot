package com.example.workflow.services;

import com.example.workflow.models.User;

import java.util.Optional;

public interface UserService {

    Optional<User> viewUser(String userId);

    User createUser(User user);

    User updateUserByPhone(String phoneNumber, User user);

    User updateProcessInstanceIdByPhoneNumber(String phoneNumber, String processInstanceId);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

}
