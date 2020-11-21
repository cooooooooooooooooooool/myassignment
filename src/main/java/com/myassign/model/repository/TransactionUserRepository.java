package com.myassign.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myassign.model.entity.Transaction;
import com.myassign.model.entity.TransactionUser;

public interface TransactionUserRepository extends JpaRepository<TransactionUser, Long> {

    public TransactionUser findByTransactionAndReceiveUserId(Transaction transaction, String receiveUserId);

    public TransactionUser findTop1ByTransactionAndReceiveUserIdNullOrderByOrderAsc(Transaction transaction);
}