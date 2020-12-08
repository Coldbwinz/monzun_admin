package com.example.monzun_admin.dto;

import com.example.monzun_admin.entities.Startup;

import java.util.Date;

public class StartupListDTO {
    private Long id;
    private String name;
    private AttachmentShortDTO logo;
    private Date createdAt;
    private Date updatedAt;

    public StartupListDTO() {
    }

    public StartupListDTO(Startup startup) {
        this.id = startup.getId();
        this.name = startup.getName();
        this.logo = startup.getLogo() != null ? new AttachmentShortDTO(startup.getLogo()) : null;
        this.createdAt = startup.getCreatedAt();
        this.updatedAt = startup.getUpdatedAt();
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
}
