package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public List<Article> getArticlesByTopicId(Long topicId) {
        return articleRepository.findByTopicId(topicId);
    }

    @Override
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Optional<Article> saveArticle(Article article) {
        return Optional.of(articleRepository.save(article));
    }
}
