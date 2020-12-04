package com.example.monzun_admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileIsEmptyException extends RuntimeException {
    public FileIsEmptyException(String msg) {
        super(msg);
    }
}
