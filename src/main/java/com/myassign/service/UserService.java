package com.myassign.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myassign.model.entity.User;
import com.myassign.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 생성
     */
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void createUser(List<User> list) {
        list.forEach(user -> {
            userRepository.save(user);
        });
    }

    /**
     * 사용자 조회
     */
    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }
}