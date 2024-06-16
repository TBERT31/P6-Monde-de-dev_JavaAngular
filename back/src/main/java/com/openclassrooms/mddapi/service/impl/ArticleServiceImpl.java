package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    public List<Article> getAllArticles(String sortBy, String order) {
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(order.toUpperCase());
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.ASC;
        }

        Sort sort;
        try {
            sort = Sort.by(sortDirection, sortBy);
        } catch (IllegalArgumentException e) {
            sort = Sort.by(sortDirection, "id");
        }
        return articleRepository.findAll(sort);
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
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }
}
