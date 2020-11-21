package com.myassign.exception;

public class TransactionExpiredException extends RuntimeException {

    private static final long serialVersionUID = -7907137683274068542L;

    public TransactionExpiredException(String token) {
        super("Transaction is expired by 10 Minutes. Your transaction token : " + token);
    }
}