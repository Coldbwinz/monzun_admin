package com.example.monzun_admin.dto;

import com.example.monzun_admin.model.Attachment;

import java.util.Date;
import java.util.UUID;

public class AttachmentShortDTO {
    private UUID uuid;
    private String url;
    private String originalFilename;
    private Date createdAt;

    public AttachmentShortDTO(Attachment attachment) {
        this.uuid = attachment.getUuid();
        this.url = attachment.getUrl();
        this.originalFilename = attachment.getOriginalFilename();
        this.createdAt = attachment.getCreatedAt();
    }

    public AttachmentShortDTO() {}

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

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
