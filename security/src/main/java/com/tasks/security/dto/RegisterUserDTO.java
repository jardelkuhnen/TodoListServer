package com.tasks.security.dto;

import com.tasks.security.enums.RoleAccess;
import com.tasks.security.model.User;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Email not valid!")
    private String email;

    @NotEmpty(message = "Password cannot be empty!")
    private String password;

    private String role;

    public static User of(RegisterUserDTO registerUserDTO) {
        User user = new User();
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(registerUserDTO.getPassword()));
        user.setRole(RoleAccess.valueOf("ROLE_"+ registerUserDTO.getRole().toUpperCase()));

        return user;
    }
}
