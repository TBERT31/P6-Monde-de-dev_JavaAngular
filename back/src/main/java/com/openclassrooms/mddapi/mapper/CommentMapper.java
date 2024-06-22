package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
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
            @Mapping(target = "user", expression = "java(mapToUser(commentDto.getUsername()))"),
            @Mapping(target = "article", expression = "java(mapToArticle(commentDto.getArticle_id()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract Comment toEntity(CommentDto commentDto);

    /**
     * Convertit un nom d'utilisateur en User.
     * @param username le nom d'utilisateur à convertir.
     * @return le User converti.
     */
    public User mapToUser(String username) {
        return username != null ? userService.getUserByUsername(username).orElse(null) : null;
    }

    /**
     * Convertit un identifiant d'article en Article.
     * @param articleId l'identifiant de l'article à convertir.
     * @return l'Article converti.
     */
    public Article mapToArticle(Long articleId) {
        return articleId != null ? articleService.getArticleById(articleId).orElse(null) : null;
    }

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "message", target = "message"),
            @Mapping(source = "comment.user.username", target = "username"),
            @Mapping(source = "comment.article.id", target = "article_id"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract CommentDto toDto(Comment comment);
}
