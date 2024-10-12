package com.projects.todoapp.DTO;

import com.projects.todoapp.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter()
@Setter()
public class TaskRequestDto {

    @NotBlank(message = "Task name cannot be null")
    private String  taskName;
    private String taskDescription;
    private TaskStatus taskStatus;
    @NotNull(message = "Due date must required")
    private long dueDate;

}
