package com.tasks.security.service;

import com.tasks.security.model.User;
import com.tasks.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User loadUserByEmail(String userName) {
        User user = this.userRepository.findByEmail(userName);

        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("Email not found!");
    }
}
