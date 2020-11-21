package com.myassign.config;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.myassign.exception.RoomNotExistException;
import com.myassign.exception.ServiceException;
import com.myassign.exception.TransactionExpiredException;
import com.myassign.exception.TransactionNotFoundException;
import com.myassign.exception.TransactionPermissionException;
import com.myassign.exception.UserNotFoundException;
import com.myassign.model.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(status.toString(), ex.getLocalizedMessage());
        log.warn("handleException : {}", ex.getLocalizedMessage());
        return super.handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> error(HttpServletResponse response, ServiceException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFound(HttpServletResponse response, UserNotFoundException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(RoomNotExistException.class)
    public ResponseEntity<Object> roomNotExistException(HttpServletResponse response, RoomNotExistException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Object> transactionNotFound(HttpServletResponse response, TransactionNotFoundException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> tokenExpiredException(HttpServletResponse response, TokenExpiredException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TransactionPermissionException.class)
    public ResponseEntity<Object> transactionPermissionException(HttpServletResponse response, TransactionPermissionException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(TransactionExpiredException.class)
    public ResponseEntity<Object> transactionExpiredException(HttpServletResponse response, TransactionExpiredException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.BAD_REQUEST, request);
    }
}