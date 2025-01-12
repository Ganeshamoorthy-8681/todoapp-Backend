package com.projects.todoapp.exception;

public class TaskNotFoundException extends  RuntimeException {
    public TaskNotFoundException(){
       super("Task Not Found Exeception");
    }
}
