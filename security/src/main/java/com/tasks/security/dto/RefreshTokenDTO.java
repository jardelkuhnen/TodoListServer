package com.tasks.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class RefreshTokenDTO {

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Email not valid!")
    private String email;
}
