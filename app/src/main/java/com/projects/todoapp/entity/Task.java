package com.projects.todoapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projects.todoapp.enums.TaskCategory;
import com.projects.todoapp.enums.TaskPriority;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private TaskCategory category;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //Used to Update the time stamp in table before creation
    @PrePersist
    protected void updateCreatedOn() {
        createdOn = Instant.now().toEpochMilli();
    }

    //Used to Update the time stamp in table before update
    @PreUpdate
    private void  updateUpdatedOn(){
        updatedOn = Instant.now().toEpochMilli();
    }
}
