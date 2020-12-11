package com.example.monzun_admin.request;


import com.example.monzun_admin.validation.rules.UniqueUserEmail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {

    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    @UniqueUserEmail
    private String email;
    @NotNull(message = "Phone is required")
    @Pattern(
            regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[0-9]{3}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$",
            message = "Phone format is invalid"
    )
    private String phone;
    private boolean isBlocked;
    private String blockReason;
}
