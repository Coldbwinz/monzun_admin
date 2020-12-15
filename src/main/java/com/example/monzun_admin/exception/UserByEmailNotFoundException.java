package com.example.monzun_admin.exception;

import org.springframework.security.core.AuthenticationException;

public class UserByEmailNotFoundException extends AuthenticationException {
    public UserByEmailNotFoundException(String msg) {
        super(msg);
    }
}
