package com.openclassrooms.mddapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    @NotBlank(message = "Message is required")
    @Size(max = 2000)
    private String message;

    @NotNull(message = "User is required")
    private Long user_id;

    @NotNull(message = "Article is required")
    private Long article_id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
