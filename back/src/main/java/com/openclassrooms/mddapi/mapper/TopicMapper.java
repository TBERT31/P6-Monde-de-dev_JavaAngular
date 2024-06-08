package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.model.Article;
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
                Topic.class, User.class, Collections.class, Optional.class}
)
public abstract class TopicMapper implements EntityMapper<TopicDto, Topic>   {

    @Autowired
    ArticleService articleService;

    @Autowired
    UserService userService;


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "description", target = "description"),
            @Mapping(target = "articles", expression = "java(Optional.ofNullable(topicDto.getArticles())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(article_id -> {Article article = this.articleService.getArticleById(article_id).orElse(null); " +
                    "if (article != null) { return article; } return null; }).collect(Collectors.toList()))"),
            @Mapping(target = "users_subscribed", expression = "java(Optional.ofNullable(topicDto.getUsers_subscribed())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(user_id -> { User user = this.userService.getUserById(user_id).orElse(null); " +
                    "if (user != null) { return user; } return null; }).collect(Collectors.toList()))"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract Topic toEntity(TopicDto topicDto);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "description", target = "description"),
            @Mapping(target = "articles", expression = "java(Optional.ofNullable(topic.getArticles())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(article -> { return article.getId(); }).collect(Collectors.toList())" +
                    ")"),
            @Mapping(target = "users_subscribed", expression = "java(Optional.ofNullable(topic.getUsers_subscribed())" +
                    ".orElseGet(Collections::emptyList).stream()" +
                    ".map(user -> { return user.getId(); }).collect(Collectors.toList())" +
                    ")"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy/MM/dd")
    })
    public abstract TopicDto toDto(Topic topic);

}
