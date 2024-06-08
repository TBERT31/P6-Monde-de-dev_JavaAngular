package com.openclassrooms.mddapi.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Username or email is required")
    private String emailOrUsername;

    @NotBlank(message = "Password is required")
    private String password;
}
