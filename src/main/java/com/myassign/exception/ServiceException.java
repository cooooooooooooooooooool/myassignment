package com.myassign.exception;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -6700032427513338274L;

    public ServiceException(String message) {
        super(message);
    }
}