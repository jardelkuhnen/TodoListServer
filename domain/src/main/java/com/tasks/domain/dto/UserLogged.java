package com.tasks.domain.dto;

import com.tasks.domain.enums.RoleAccess;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class UserLogged {

    private Long id;

    private String email;

    private RoleAccess role;

}
