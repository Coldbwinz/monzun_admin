package com.example.monzun_admin.response;

import com.example.monzun_admin.model.User;

public class UserListResponse {
    private Long id;
    private String name;

    public UserListResponse (User user) {
        this.id = user.getId();
        this.name = user.getName();
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
}
