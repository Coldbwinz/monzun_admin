package com.example.monzun_admin.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "attachments", schema = "public")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

}