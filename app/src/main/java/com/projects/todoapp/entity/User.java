package com.projects.todoapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    private int userId;

    @Column(name = "name")
    private String username;

    @Column(name = "email")
    private String email;

    // In order to avoid fetching the user entity only send the user id.
    // user can be taken by using the getter
    //Join column used for mention the foreign key mapping with user table
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<Task> taskList;

}
