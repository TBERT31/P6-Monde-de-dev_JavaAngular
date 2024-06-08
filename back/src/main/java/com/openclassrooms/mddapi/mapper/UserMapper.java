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

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    public List<Comment> mapToCommentList(List<Long> commentIds) {
        return Optional.ofNullable(commentIds)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(commentId -> commentService.getCommentById(commentId).orElse(null))
                .filter(comment -> comment != null)
                .collect(Collectors.toList());
    }

    public List<Article> mapToArticleList(List<Long> articleIds) {
        return Optional.ofNullable(articleIds)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(articleId -> articleService.getArticleById(articleId).orElse(null))
                .filter(article -> article != null)
                .collect(Collectors.toList());
    }

    public List<Topic> mapToTopicList(List<Long> topicIds) {
        return Optional.ofNullable(topicIds)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(topicId -> topicService.getTopicById(topicId).orElse(null))
                .filter(topic -> topic != null)
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

    public List<Long> mapToCommentIds(List<Comment> comments) {
        return Optional.ofNullable(comments)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
    }

    public List<Long> mapToArticleIds(List<Article> articles) {
        return Optional.ofNullable(articles)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Article::getId)
                .collect(Collectors.toList());
    }

    public List<Long> mapToTopicIds(List<Topic> topics) {
        return Optional.ofNullable(topics)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Topic::getId)
                .collect(Collectors.toList());
    }
}
