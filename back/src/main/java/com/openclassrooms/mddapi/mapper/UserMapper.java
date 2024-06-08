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
            @Mapping(target = "comments", expression = "java(Optional.ofNullable(userDto.getComments())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(comment_id -> {Comment comment = this.commentService.getCommentById(comment_id).orElse(null); " +
                    "if (comment != null) { return comment; } return null; }).collect(Collectors.toList()))"),
            @Mapping(target = "articles", expression = "java(Optional.ofNullable(userDto.getArticles())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(article_id -> {Article article = this.articleService.getArticleById(article_id).orElse(null); " +
                    "if (article != null) { return article; } return null; }).collect(Collectors.toList()))"),
            @Mapping(target = "topics_subscribed", expression = "java(Optional.ofNullable(userDto.getTopics_subscribed())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(topic_id -> {Topic topic = this.topicService.getTopicById(topic_id).orElse(null); " +
                    "if (topic != null) { return topic; } return null; }).collect(Collectors.toList()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract User toEntity(UserDto userDto);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "email", target = "email"),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "comments", expression = "java(Optional.ofNullable(user.getComments())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(comment -> { return comment.getId(); }).collect(Collectors.toList())" +
                    ")"),
            @Mapping(target = "articles", expression = "java(Optional.ofNullable(user.getArticles())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(article -> { return article.getId(); }).collect(Collectors.toList())" +
                    ")"),
            @Mapping(target="topics_subscribed", expression = "java(Optional.ofNullable(user.getTopics_subscribed())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(topic -> { return topic.getId(); }).collect(Collectors.toList())" +
                    ")"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract UserDto toDto(User user);
}
