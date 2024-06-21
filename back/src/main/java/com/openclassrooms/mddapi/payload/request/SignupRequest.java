package com.openclassrooms.mddapi.payload.request;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 than 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max=50, message = "Email must be less than 50 characters")
    @Email(message = "Email is not compliant")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 120, message = "Password must be between 8 and 120 characters")
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=,?;./:!§£*()-_¨µ<>{}]).{8,}",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character and at least 8 characters"
    )
    private String password;
}

