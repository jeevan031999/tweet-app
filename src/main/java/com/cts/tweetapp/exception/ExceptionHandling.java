package com.cts.tweetapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler({ Exception_UserAlreadyExists.class })
    public ResponseEntity<ErrorMessage> handleExceptionUserAlreadyExists(Exception_UserAlreadyExists ex) {
        ErrorMessage response = new ErrorMessage(LocalDateTime.now(), HttpStatus.UNAUTHORIZED,
                ex.getMessage(), "User Already Exists");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ Exception_UserDoesNotExists.class })
    public ResponseEntity<ErrorMessage> handleExceptionUserDoesNotExists(Exception_UserDoesNotExists ex) {
        ErrorMessage response = new ErrorMessage(LocalDateTime.now(), HttpStatus.UNAUTHORIZED,
                ex.getMessage(), "No user found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
