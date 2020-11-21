package com.myassign.exception;

import java.util.UUID;

public class TransactionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7907137683274068542L;

    public TransactionNotFoundException(UUID roomId, String token) {
        super("Transaction not found, room : " + roomId + ", token : " + token);
    }
}