package com.jm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}