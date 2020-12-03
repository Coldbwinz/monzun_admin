package com.example.monzun_admin.exception;

import org.springframework.security.core.AuthenticationException;

public class UserByEmailNotFound extends AuthenticationException {
    public UserByEmailNotFound(String msg) {
        super(msg);
    }
}
