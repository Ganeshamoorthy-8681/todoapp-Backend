package com.projects.todoapp.entity;

import com.projects.todoapp.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity(name = "task")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String taskName;

    @Column(name = "description")
    private  String taskDescription;

    @Column(name = "created",updatable = false,nullable = false)
    private long createdOn;

    @Column(name = "updated")
    private long updatedOn;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Column(name = "due_date")
    private long dueDate;

    @PrePersist
    protected void updateCreatedOn() {
        createdOn = Instant.now().toEpochMilli();
    }

    @PreUpdate
    private void  updateUpdatedOn(){
        updatedOn = Instant.now().toEpochMilli();
    }
}
