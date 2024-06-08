package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max = 50)
    @Email
    private String email;

    @JsonIgnore
    @Size(max = 120)
    private String password;

    private List<Long> comments;

    private List<Long> articles;

    private List<Long> topics_subscribed;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private String createdAt;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private String updatedAt;
}
