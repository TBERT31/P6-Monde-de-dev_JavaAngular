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
import java.util.List;
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
            @Mapping(target = "author", expression = "java(mapToUser(articleDto.getAuthor()))"),
            @Mapping(target = "topic", expression = "java(mapToTopic(articleDto.getTopic_id()))"),
            @Mapping(target = "comments", expression = "java(mapToComments(articleDto.getComments()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract Article toEntity(ArticleDto articleDto);

    public User mapToUser(String username) {
        return username != null ? userService.getUserByUsername(username).orElse(null) : null;
    }

    public Topic mapToTopic(Long topicId) {
        return topicId != null ? topicService.getTopicById(topicId).orElse(null) : null;
    }

    public List<Comment> mapToComments(List<Long> commentIds) {
        return Optional.ofNullable(commentIds)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(commentId -> commentService.getCommentById(commentId).orElse(null))
                .filter(comment -> comment != null)
                .collect(Collectors.toList());
    }

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "article.author.username", target = "author"),
            @Mapping(source = "article.topic.id", target = "topic_id"),
            @Mapping(target = "comments", expression = "java(mapToCommentIds(article.getComments()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract ArticleDto toDto(Article article);

    public List<Long> mapToCommentIds(List<Comment> comments) {
        return Optional.ofNullable(comments)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
    }
}
