package com.mvcduplicate.demo.service;

import com.mvcduplicate.demo.model.User;
import com.mvcduplicate.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user)
    {

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
        throw new RuntimeException("Username already exists");
    }
        user.setPassword((user.getPassword()));
        userRepository.save(user);
        return "user registered successfully";

    }
}
