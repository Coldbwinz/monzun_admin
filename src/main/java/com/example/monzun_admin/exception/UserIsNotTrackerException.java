package com.example.monzun_admin.exception;

import com.example.monzun_admin.entities.User;

public class UserIsNotTrackerException extends Exception {
    public UserIsNotTrackerException(User user) {
        super("User is not tracker with id" + user.getId());
    }
}
