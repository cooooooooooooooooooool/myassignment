package com.myassign.exception;

import java.util.UUID;

public class RoomNotExistException extends RuntimeException {

    private static final long serialVersionUID = -7907137683274068542L;

    public RoomNotExistException(UUID roomId) {
        super("room doesn't exist : " + roomId);
    }

    public RoomNotExistException(String message) {
        super(message);
    }
}