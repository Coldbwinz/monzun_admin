package com.example.monzun_admin.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Роли в системе. Подразумевается 3 роли, для удобства описаны в enum.
 */
@Entity
@Table(name = "roles", schema = "public")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "title")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return roleId == role.roleId;
    }

    public Role() {
    }

    public Role(int roleId, String name, String title) {
        this.roleId = roleId;
        this.name = name;
        this.title = title;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}
