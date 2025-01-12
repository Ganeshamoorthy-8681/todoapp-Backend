package com.projects.todoapp.consumer;

import com.projects.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AccountCreationConsumer {

    @Autowired
    private UserService userService;

    // Define the topic and group ID for consumption
    @KafkaListener(topics = "account_create",groupId = "account_creation")
    public void consume(HashMap<String,Object> message) {
                 String username  = (String) message.get("username");
                 String email = (String) message.get("email");
                 Integer id = (Integer) message.get("userId");
            this.userService.createUser(username,email,id);
            System.out.println("Message received: " + message.get("username") + " Account Created Successful");
    }
}
