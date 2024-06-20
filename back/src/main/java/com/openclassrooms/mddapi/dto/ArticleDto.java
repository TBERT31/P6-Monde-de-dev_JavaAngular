package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Size(max = 10000)
    private String content;

    @NotNull(message = "Author is required")
    private String author;

    @NotNull(message = "Topic_title is required")
    private String topic_title;

    private List<Long> comments;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private String createdAt;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private String updatedAt;
}
