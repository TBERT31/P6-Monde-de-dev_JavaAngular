package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.TopicService;
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
        uses = {ArticleService.class, UserService.class, TopicService.class},
        imports = {Arrays.class, Collectors.class, Article.class, Topic.class,
                Comment.class, User.class, Collections.class, Optional.class}
)
public abstract class ArticleMapper implements EntityMapper<ArticleDto, Article>{
    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    TopicService topicService;

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(target = "author", expression = "java(articleDto.getAuthor_id() != null ? " +
                    "this.userService.getUserById(articleDto.getAuthor_id()).orElse(null) : null)"),
            @Mapping(target = "topic", expression = "java(articleDto.getTopic_id() != null ? " +
                    "this.topicService.getTopicById(articleDto.getTopic_id()).orElse(null) : null)"),
            @Mapping(target = "comments", expression = "java(Optional.ofNullable(articleDto.getComments())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(comment_id -> { Comment comment = this.commentService.getCommentById(comment_id).orElse(null);" +
                    "if (comment != null) { return comment; } return null; }).collect(Collectors.toList()))"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    public abstract Article toEntity(ArticleDto articleDto);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "article.author.id", target = "author_id"),
            @Mapping(source = "article.topic.id", target = "topic_id"),
            @Mapping(target = "comments", expression = "java(Optional.ofNullable(article.getComments())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(comment -> { return comment.getId(); }).collect(Collectors.toList())" +
                    ")"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    public abstract ArticleDto toDto(Article article);
}
