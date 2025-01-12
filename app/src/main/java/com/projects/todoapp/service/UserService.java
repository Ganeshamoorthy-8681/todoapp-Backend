package com.projects.todoapp.service;

import com.projects.todoapp.entity.User;
import com.projects.todoapp.exception.UserNotFoundException;
import com.projects.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(String username, String email, int id){
        User user = new User();
        user.setEmail(email);
        user.setUserId(id);
        user.setUsername(username);
        this.userRepository.save(user);
    }

    public User getUser(int userId) throws UserNotFoundException {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }else {
            throw  new UserNotFoundException("User not Found Exception");
        }
    }
}
