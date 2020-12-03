package com.example.monzun_admin.enums;

public enum RoleEnum {
    ADMIN(1),
    STARTUP(2),
    TRACKER(3);

    private final int roleId;

    RoleEnum(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return this.roleId;
    }
}
