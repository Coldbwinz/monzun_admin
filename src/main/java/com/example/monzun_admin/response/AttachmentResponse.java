package com.example.monzun_admin.response;

import com.example.monzun_admin.model.Attachment;

import java.util.Date;
import java.util.UUID;

public class AttachmentResponse {
    private UUID uuid;
    private String fileName;
    private String originalFilename;
    private String path;
    private UserListResponse owner;
    private Date createdAt;

    public AttachmentResponse(Attachment attachment) {
        this.uuid = attachment.getUuid();
        this.fileName = attachment.getFilename();
        this.originalFilename = attachment.getOriginalFilename();
        this.path = attachment.getPath();
        this.owner = new UserListResponse(attachment.getOwner());
        this.createdAt = attachment.getCreatedAt();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public UserListResponse getOwner() {
        return owner;
    }

    public void setOwner(UserListResponse owner) {
        this.owner = owner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
