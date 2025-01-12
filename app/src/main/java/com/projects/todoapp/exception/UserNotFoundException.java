package com.projects.todoapp.exception;

public class UserNotFoundException extends Exception {
    public  UserNotFoundException(String message){
        super(message);
    }
}
