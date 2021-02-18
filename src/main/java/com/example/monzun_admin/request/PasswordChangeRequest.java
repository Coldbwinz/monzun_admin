package com.example.monzun_admin.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {
    @NotNull(message = "token is required")
    private String token;
    @NotNull(message = "password is required")
    private String newPassword;
}
