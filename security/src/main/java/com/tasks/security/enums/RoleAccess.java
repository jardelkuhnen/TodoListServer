package com.tasks.security.enums;

public enum RoleAccess {

    ROLE_ADMIN(0),
    ROLE_USER(1),
    ROLE_VISITOR(2);

    private Integer ordinal;

    RoleAccess(Integer ordinal) {
        this.ordinal = ordinal;
    }
}
