package com.example.monzun_admin.model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "attachments", schema = "public")
public class Attachment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "attachment_id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "uuid")
    private UUID uuid = UUID.randomUUID();
    @Column(name = "url")
    private String url;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @Column(name = "filename", nullable = false)
    private String filename;
    @Column(name = "original_filename")
    private String originalFilename;
    @Column(name = "path", nullable = false)
    private String path;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
