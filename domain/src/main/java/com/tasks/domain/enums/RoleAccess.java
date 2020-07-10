package com.tasks.domain.enums;

import lombok.Getter;

@Getter
public enum RoleAccess {

    ROLE_ADMIN(0, "ADMIN"),
    ROLE_USER(1, "USER"),
    ROLE_VISITOR(2, "VISITOR");

    private Integer ordinal;
    private String description;

    RoleAccess(Integer ordinal, String description) {
        this.ordinal = ordinal;
        this.description = description;
    }
}
