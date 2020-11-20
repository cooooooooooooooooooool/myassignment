package com.myassign.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myassign.model.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}