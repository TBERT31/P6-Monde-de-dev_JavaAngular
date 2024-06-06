package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<Article> getAllArticles();
    List<Article> getArticlesByTopicId(Long topicId);
    Optional<Article> getArticleById(Long id);
    Optional<Article> saveArticle(Article article);
}
