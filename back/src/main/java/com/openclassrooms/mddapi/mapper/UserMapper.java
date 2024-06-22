package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.TopicService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring",
        uses = {CommentService.class, ArticleService.class, TopicService.class},
        imports = {Arrays.class, Collectors.class, Comment.class, Article.class,
                Topic.class, User.class, Collections.class, Optional.class}
)
public abstract class UserMapper implements EntityMapper<UserDto, User>  {

    @Autowired
    CommentService commentService;

    @Autowired
    ArticleService articleService;

    @Autowired
    TopicService topicService;


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "comments", expression = "java(mapToCommentList(userDto.getComments()))"),
            @Mapping(target = "articles", expression = "java(mapToArticleList(userDto.getArticles()))"),
            @Mapping(target = "topics_subscribed", expression = "java(mapToTopicList(userDto.getTopics_subscribed()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract User toEntity(UserDto userDto);

    /**
     * Convertit une liste d'identifiants de commentaires en une liste de Comment.
     * @param commentIds la liste d'identifiants de commentaires à convertir.
     * @return la liste de Comment convertie.
     */
    public List<Comment> mapToCommentList(List<Long> commentIds) {
        return Optional.ofNullable(commentIds)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(commentId -> commentService.getCommentById(commentId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'identifiants d'articles en une liste d'Article.
     * @param articleIds la liste d'identifiants d'articles à convertir.
     * @return la liste d'Article convertie.
     */
    public List<Article> mapToArticleList(List<Long> articleIds) {
        return Optional.ofNullable(articleIds)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(articleId -> articleService.getArticleById(articleId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'identifiants de sujets en une liste de Topic.
     * @param topicIds la liste d'identifiants de sujets à convertir.
     * @return la liste de Topic convertie.
     */
    public List<Topic> mapToTopicList(List<Long> topicIds) {
        return Optional.ofNullable(topicIds)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(topicId -> topicService.getTopicById(topicId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "email", target = "email"),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "comments", expression = "java(mapToCommentIds(user.getComments()))"),
            @Mapping(target = "articles", expression = "java(mapToArticleIds(user.getArticles()))"),
            @Mapping(target="topics_subscribed", expression = "java(mapToTopicIds(user.getTopics_subscribed()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract UserDto toDto(User user);

    /**
     * Convertit une liste de Comment en une liste d'identifiants de commentaires.
     * @param comments la liste de Comment à convertir.
     * @return la liste d'identifiants de commentaires convertie.
     */
    public List<Long> mapToCommentIds(List<Comment> comments) {
        return Optional.ofNullable(comments)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'Article en une liste d'identifiants d'articles.
     * @param articles la liste d'Article à convertir.
     * @return la liste d'identifiants d'articles convertie.
     */
    public List<Long> mapToArticleIds(List<Article> articles) {
        return Optional.ofNullable(articles)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Article::getId)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de Topic en une liste d'identifiants de sujets.
     * @param topics la liste de Topic à convertir.
     * @return la liste d'identifiants de sujets convertie.
     */
    public List<Long> mapToTopicIds(List<Topic> topics) {
        return Optional.ofNullable(topics)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Topic::getId)
                .collect(Collectors.toList());
    }
}
