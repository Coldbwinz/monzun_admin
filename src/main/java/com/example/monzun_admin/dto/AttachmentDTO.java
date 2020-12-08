package com.example.monzun_admin.dto;

import com.example.monzun_admin.entities.Attachment;

import java.util.Date;
import java.util.UUID;

public class AttachmentDTO {
    private UUID uuid;
    private String fileName;
    private String url;
    private String originalFilename;
    private String path;
    private UserListDTO owner;
    private Date createdAt;

    public AttachmentDTO(Attachment attachment) {
        this.uuid = attachment.getUuid();
        this.url = attachment.getUrl();
        this.fileName = attachment.getFilename();
        this.originalFilename = attachment.getOriginalFilename();
        this.path = attachment.getPath();
        this.owner = new UserListDTO(attachment.getOwner());
        this.createdAt = attachment.getCreatedAt();
    }

    public AttachmentDTO() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UserListDTO getOwner() {
        return owner;
    }

    public void setOwner(UserListDTO owner) {
        this.owner = owner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
