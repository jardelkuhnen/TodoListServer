package com.tasks.domain.service;

import com.tasks.domain.dto.UserLogged;
import com.tasks.domain.enums.RoleAccess;
import com.tasks.domain.exception.NotFoundException;
import com.tasks.domain.model.User;
import com.tasks.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemUtilsService {

    private final String USER_ID = "userId";
    private final String USER_EMAIL = "userEmail";
    private final String USER_ROLE = "userRole";

    private final UserRepository userRepository;

    @Autowired
    public SystemUtilsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void registerUser(String username) {

        User user = this.userRepository.findByEmail(username);

        if (user == null) {
            throw new NotFoundException("Usuário não encontrado!");
        }

        System.setProperty(USER_ID, user.getId().toString());
        System.setProperty(USER_EMAIL, user.getEmail());
        System.setProperty(USER_ROLE, "ROLE_" + user.getRole().getDescription());

    }

    public UserLogged getUserLogged() {
        return UserLogged.builder()
                .id(Long.parseLong(System.getProperty(USER_ID)))
                .email(System.getProperty(USER_EMAIL))
                .role(RoleAccess.valueOf(System.getProperty(USER_ROLE)))
                .build();
    }

}
