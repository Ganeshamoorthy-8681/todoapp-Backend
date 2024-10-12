package com.projects.todoapp.DTO;

import com.projects.todoapp.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TaskResponseDto {

    private int id;

    private String taskName;

    private  String taskDescription;

    private long createdOn;

    private long updatedOn;

    private TaskStatus taskStatus;

    private  long dueDate;


}
