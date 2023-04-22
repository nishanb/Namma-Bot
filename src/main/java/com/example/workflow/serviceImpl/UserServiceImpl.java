package com.example.workflow.serviceImpl;


import com.example.workflow.models.User;
import com.example.workflow.repository.UserRepository;
import com.example.workflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<User> viewUser(String userId) {
        return Optional.of(userRepository.findById(userId).get());
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userId, User user) {
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    @Override
    public User updateProcessInstanceIdByUserId(String userId, String processInstanceId) {
        Optional<User> user = Optional.of(viewUser(userId).get());
        User userSaved = user.get();
        userSaved.setProcessInstanceId(processInstanceId);
        return updateUser(userId, userSaved);
    }

    @Override
    public User updateProcessInstanceIdByPhoneNumber(String phoneNumber, String processInstanceId) {
        Optional<User> user = Optional.of(findUserByPhoneNumber(phoneNumber).get());
        User userSaved = user.get();
        userSaved.setProcessInstanceId(processInstanceId);
        return updateUser(userSaved.getId(), userSaved);
    }

    @Override
    public Optional<User> findUserByProcessInstanceId(String processInstanceId) {
        return userRepository.findUserByProcessInstanceId(processInstanceId);
    }

    @Override
    public Optional<User> findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findUserByPhoneNumber(phoneNumber);
    }

    @Override
    public User updateUserLanguageByPhoneNumber(String phoneNumber, String preferredLanguage) {
        Optional<User> user = Optional.of(findUserByPhoneNumber(phoneNumber).get());
        User userSaved = user.get();
        userSaved.setPreferredLanguage(preferredLanguage);
        return updateUser(userSaved.getId(), userSaved);
    }
}
