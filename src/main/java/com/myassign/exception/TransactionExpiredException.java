package com.myassign.exception;

import java.util.UUID;

public class TransactionExpiredException extends RuntimeException {

    private static final long serialVersionUID = -7907137683274068542L;

    public TransactionExpiredException(String token, UUID roomId) {
        super("Transaction is exceeded during 10 Minutes. Your transaction token : " + token + ", roomId : " + roomId);
    }

    public TransactionExpiredException(String message) {
        super(message);
    }
}