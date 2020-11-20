package com.myassign.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myassign.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}