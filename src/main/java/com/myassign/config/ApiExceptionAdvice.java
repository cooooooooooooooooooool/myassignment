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
import com.myassign.exception.AccessTokenEmptyException;
import com.myassign.exception.AccessTokenInvalidException;
import com.myassign.exception.ServiceException;
import com.myassign.exception.UserNotFoundException;
import com.myassign.exception.UserPasswordWrongException;
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

    @ExceptionHandler(UserPasswordWrongException.class)
    public ResponseEntity<Object> passwordWrong(HttpServletResponse response, UserPasswordWrongException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(AccessTokenInvalidException.class)
    public ResponseEntity<Object> accessTokenInvalid(HttpServletResponse response, AccessTokenInvalidException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AccessTokenEmptyException.class)
    public ResponseEntity<Object> accessTokenEmpty(HttpServletResponse response, AccessTokenEmptyException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> tokenExpiredException(HttpServletResponse response, TokenExpiredException ex, WebRequest request) throws IOException {
        return handleExceptionInternal(ex, null, null, HttpStatus.UNAUTHORIZED, request);
    }
}