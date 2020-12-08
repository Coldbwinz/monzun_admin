package com.example.monzun_admin.dto;

import com.example.monzun_admin.entities.Role;
import com.example.monzun_admin.entities.User;

import java.util.Date;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean isBlocked;
    private String blockReason;
    private AttachmentShortDTO logo;
    private Role role;
    private Date createdAt;
    private Date updatedAt;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.logo = user.getLogo() != null ? new AttachmentShortDTO(user.getLogo()) : null;
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.isBlocked = user.isBlocked();
        this.blockReason = user.getBlockReason();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AttachmentShortDTO getLogo() {
        return logo;
    }

    public void setLogo(AttachmentShortDTO logo) {
        this.logo = logo;
    }
}
