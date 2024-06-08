package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring",
        uses = {ArticleService.class, UserService.class},
        imports = {Arrays.class, Collectors.class, Article.class,
                Comment.class, User.class, Collections.class, Optional.class}
)
public abstract class CommentMapper implements EntityMapper<CommentDto, Comment>{

    @Autowired
    ArticleService articleService;

    @Autowired
    UserService userService;

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "message", target = "message"),
            @Mapping(target = "user", expression = "java(mapToUser(commentDto.getUser_id()))"),
            @Mapping(target = "article", expression = "java(mapToArticle(commentDto.getArticle_id()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract Comment toEntity(CommentDto commentDto);

    public User mapToUser(Long userId) {
        return userId != null ? userService.getUserById(userId).orElse(null) : null;
    }

    public Article mapToArticle(Long articleId) {
        return articleId != null ? articleService.getArticleById(articleId).orElse(null) : null;
    }

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "message", target = "message"),
            @Mapping(source = "comment.user.id", target = "user_id"),
            @Mapping(source = "comment.article.id", target = "article_id"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract CommentDto toDto(Comment comment);
}
