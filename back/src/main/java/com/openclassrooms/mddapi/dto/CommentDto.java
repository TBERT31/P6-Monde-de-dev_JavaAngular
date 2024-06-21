package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    @NotBlank(message = "Message is required")
    @Size(max = 2000)
    private String message;

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Article is required")
    private Long article_id;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private String createdAt;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private String updatedAt;
}
