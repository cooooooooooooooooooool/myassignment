package com.myassign.exception;

import java.util.UUID;

public class TransactoinNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7907137683274068542L;

    public TransactoinNotFoundException(UUID roomId, String token) {
        super("Transaction not found, room : " + roomId + ", token : " + token);
    }
}