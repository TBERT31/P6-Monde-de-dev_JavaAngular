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

import java.util.*;
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
            @Mapping(target = "topic", expression = "java(mapToTopic(articleDto.getTopic_title()))"),
            @Mapping(target = "comments", expression = "java(mapToComments(articleDto.getComments()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract Article toEntity(ArticleDto articleDto);

    /**
     * Convertit un nom d'utilisateur en User.
     * @param username le nom d'utilisateur à convertir.
     * @return le User converti.
     */
    public User mapToUser(String username) {
        return username != null ? userService.getUserByUsername(username).orElse(null) : null;
    }

    /**
     * Convertit un titre de sujet en Topic.
     * @param topicTitle le titre du sujet à convertir.
     * @return le Topic converti.
     */
    public Topic mapToTopic(String topicTitle) {
        return topicTitle != null ? topicService.getTopicByTitle(topicTitle).orElse(null) : null;
    }

    /**
     * Convertit une liste d'identifiants de commentaires en une liste de Comment.
     * @param commentIds la liste d'identifiants de commentaires à convertir.
     * @return la liste de Comment convertie.
     */
    public List<Comment> mapToComments(List<Long> commentIds) {
        return Optional.ofNullable(commentIds)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(commentId -> commentService.getCommentById(commentId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "article.author.username", target = "author"),
            @Mapping(source = "article.topic.title", target = "topic_title"),
            @Mapping(target = "comments", expression = "java(mapToCommentIds(article.getComments()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract ArticleDto toDto(Article article);

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
}
