package com.projects.todoapp.DTO;

import com.projects.todoapp.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateRequestDTO {

    @NotNull
    private int id;
    @NotNull
    @NotEmpty
    private String taskName;
    private  String taskDescription;
    private long createdOn;
    @NotNull
    private TaskStatus taskStatus;
    @NotNull
    private long dueDate;
}
