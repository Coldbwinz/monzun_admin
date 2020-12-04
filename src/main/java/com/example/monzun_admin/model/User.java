package com.example.monzun_admin.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users", schema = "public")
@DiscriminatorValue("USR")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;
    @Column(name = "block_reason")
    private String blockReason;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}

