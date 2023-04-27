package com.example.workflow.serviceImpl;


import com.example.workflow.models.User;
import com.example.workflow.repository.UserRepository;
import com.example.workflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "users", key = "#phoneNumber")
    @Override
    public User updateUserByPhone(String phoneNumber, User user) {
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#phoneNumber")
    @Override
    public User updateProcessInstanceIdByPhoneNumber(String phoneNumber, String processInstanceId) {
        Optional<User> user = Optional.of(findUserByPhoneNumber(phoneNumber).get());
        User userSaved = user.get();
        userSaved.setProcessInstanceId(processInstanceId);
        return updateUserByPhone(userSaved.getPhoneNumber(), userSaved);
    }

    @Cacheable("users")
    @Override
    public Optional<User> findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findUserByPhoneNumber(phoneNumber);
    }
}