package com.tasks.security.dto;

import com.tasks.security.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String email;

    private String role;

    private String token;

    public static UserDTO of(String token, User user) {

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().getDescription())
                .token(token).build();

    }

    public static UserDTO of(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }
}
