package com.myassign.exception;

public class TransactionPermissionException extends RuntimeException {

    private static final long serialVersionUID = -7907137683274068542L;

    public TransactionPermissionException() {
        super("Beause it isn't your transaction, permission is denied.");
    }
}