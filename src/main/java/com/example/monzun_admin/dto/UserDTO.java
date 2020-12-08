package com.example.monzun_admin.dto;

import com.example.monzun_admin.entities.Role;
import com.example.monzun_admin.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
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
}
