package com.myassign.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myassign.model.entity.TransactionUser;

public interface TransactionUserRepository extends JpaRepository<TransactionUser, Long> {
}