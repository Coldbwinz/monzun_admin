package com.example.monzun_admin.enums;

import com.example.monzun_admin.entities.Role;

public enum RoleEnum {
    ADMIN(new Role(1, "admin", "Администратор")),
    STARTUP(new Role(2, "startup", "Участник")),
    TRACKER(new Role(3, "tracker", "Наблюдатель"));

    private final Role role;


    RoleEnum(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return this.role;
    }
}
