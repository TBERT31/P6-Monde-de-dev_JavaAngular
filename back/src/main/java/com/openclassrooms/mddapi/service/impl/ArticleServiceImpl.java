package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service article.
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;

    /**
     * Récupère tous les articles.
     *
     * @param sortBy le champ de tri
     * @param order l'ordre de tri
     * @return tous les articles sous forme de liste
     */
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

    /**
     * Récupère les articles par l'id de leur thème.
     *
     * @param topicId l'identifiant du thème des articles
     * @return la liste des articles qui ont pour thème le thème correspondant
     */
    @Override
    public List<Article> getArticlesByTopicId(Long topicId) {
        return articleRepository.findByTopicId(topicId);
    }

    /**
     * Récupère un article par son identifiant.
     *
     * @param id l'identifiant de l'article
     * @return l'article correspondant
     */
    @Override
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    /**
     * Sauvegarde l'article en base de données (utilisé pour une création ou une mise à jour).
     *
     * @param article l'article à sauvegarder
     * @return l'article sauvegardé
     */
    @Override
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }
}
