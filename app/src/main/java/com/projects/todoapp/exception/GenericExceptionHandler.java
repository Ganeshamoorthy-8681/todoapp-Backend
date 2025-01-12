package com.projects.todoapp.exception;

import com.projects.todoapp.exception.entity.Error;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error>handleUserNotFoundException(Exception e){
        return new ResponseEntity<>(GenericExceptionHandler.getErrorInstance(e.getMessage(), HttpServletResponse.SC_NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Error> handleInternalServerError(Exception e){
        return new ResponseEntity<>(GenericExceptionHandler.getErrorInstance(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public  ResponseEntity<Error> handleTaskNotFoundException(Exception e){
        return new ResponseEntity<>(GenericExceptionHandler.getErrorInstance(e.getMessage(), HttpServletResponse.SC_NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e){
        return new ResponseEntity<Error> (GenericExceptionHandler   .getErrorInstance(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private static Error getErrorInstance(String errorMessage, int statusCode){
        return new Error(statusCode,errorMessage);
    }
}