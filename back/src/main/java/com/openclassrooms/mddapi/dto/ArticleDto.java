package com.openclassrooms.mddapi.dto;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 50)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 2000)
    private String content;

    @NotNull(message = "Author is required")
    private Long author_id;

    @NotNull(message = "Topic is required")
    private Long topic_id;

    private List<Long> comments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
