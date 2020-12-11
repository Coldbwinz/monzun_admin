package com.example.monzun_admin.request;

import com.example.monzun_admin.validation.rules.ExistsUserEmail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {
    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    @ExistsUserEmail
    private String email;
    @NotNull(message = "Password is required")
    private String password;
}
