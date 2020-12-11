package com.example.monzun_admin.dto;

import com.example.monzun_admin.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserListDTO {
    private Long id;
    private String name;
    private AttachmentShortDTO logo;

    public UserListDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.logo = user.getLogo() != null ? new AttachmentShortDTO(user.getLogo()) : null;
    }
}
