package com.example.monzun_admin.dto;

import com.example.monzun_admin.entities.User;

public class UserListDTO {
    private Long id;
    private String name;
    private AttachmentShortDTO logo;

    public UserListDTO() {
    }

    public UserListDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.logo = user.getLogo() != null ? new AttachmentShortDTO(user.getLogo()) : null;
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

    public AttachmentShortDTO getLogo() {
        return logo;
    }

    public void setLogo(AttachmentShortDTO logo) {
        this.logo = logo;
    }
}
