package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 50)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 2000)
    private String description;

    private List<Long> articles;

    private List<Long> users_subscribed;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private String createdAt;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private String updatedAt;
}
