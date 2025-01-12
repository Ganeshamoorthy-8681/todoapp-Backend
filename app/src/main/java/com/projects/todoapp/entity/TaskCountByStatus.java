package com.projects.todoapp.entity;

import com.projects.todoapp.enums.TaskStatus;

public interface TaskCountByStatus {
    Long getCount();
    TaskStatus getStatus();
}
