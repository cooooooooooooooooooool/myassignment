package com.jm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.model.entity.User;
import com.jm.model.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자 저장
     */
    public void addUser(User user) throws RuntimeException {
        userRepository.save(user);
    }

    /**
     * 사용자 조회
     */
    public User getUser(String id) throws RuntimeException {
        return userRepository.findById(id).orElse(null);
    }
}