package com.tasks.security.service;

import com.tasks.security.dto.RegisterUserDTO;
import com.tasks.security.dto.ResetPasswordDTO;
import com.tasks.domain.dto.UserDTO;
import com.tasks.domain.exception.NotFoundException;
import com.tasks.domain.model.User;
import com.tasks.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public UserDTO registerUser(RegisterUserDTO registerUserDTO) throws Exception {

        this.validateEmailRegistered(registerUserDTO);

        User user = RegisterUserDTO.of(registerUserDTO);

        user = this.userRepository.save(user);

        return UserDTO.of(user);
    }

    private void validateEmailRegistered(RegisterUserDTO registerUserDTO) throws Exception {
        User existsUser = this.userRepository.findByEmail(registerUserDTO.getEmail());

        if (existsUser != null) {
            throw new Exception("Email alredy registered");
        }
    }

    public UserDTO resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {

        this.checkConfirmPassword(resetPasswordDTO);

        User user = this.userRepository.findByEmail(resetPasswordDTO.getEmail());

        if (Objects.isNull(user)) {
            throw new NotFoundException("User not found!");
        }

        user.setPassword(new BCryptPasswordEncoder().encode(resetPasswordDTO.getPassword()));

        this.userRepository.save(user);

        return UserDTO.of(user);
    }

    private void checkConfirmPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {

        if (!resetPasswordDTO.getPassword().equalsIgnoreCase(resetPasswordDTO.getConfirmPassword())) {
            throw new Exception("Passwords are not equal!");
        }

    }

    public void registerUserLoged(String username) {

        User user = this.userRepository.findByEmail(username);

        if (user == null) {
            throw new NotFoundException("User not found wiht email: " + username);
        }

        System.setProperty("userLoggedName", user.getEmail());
        System.setProperty("userLoggedId", user.getId().toString());

    }
}
