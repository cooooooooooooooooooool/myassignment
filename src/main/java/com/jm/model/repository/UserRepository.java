package com.jm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.model.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}