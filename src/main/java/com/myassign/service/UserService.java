package com.myassign.service;

import org.springframework.stereotype.Service;

import com.myassign.model.entity.User;
import com.myassign.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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