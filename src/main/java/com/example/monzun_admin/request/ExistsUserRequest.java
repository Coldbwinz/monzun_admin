package com.example.monzun_admin.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class ExistsUserRequest extends UserRequest {
    @NotNull(message = "Name is required")
    @ApiModelProperty(required = true)
    private String name;
    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    @ApiModelProperty(required = true)
    private String email;
    @NotNull(message = "Phone is required")
    @Pattern(
            regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[0-9]{3}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$",
            message = "Phone format is invalid"
    )
    private String phone;
    @ApiModelProperty(required = true)
    private boolean isBlocked;
    private String blockReason;
}
