package com.tasks.security.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Email not valid!")
    private String email;

    @NotEmpty(message = "Password cannot be empty!")
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty!")
    private String confirmPassword;
}
