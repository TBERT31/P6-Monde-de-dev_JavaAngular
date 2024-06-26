package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.ForbiddenException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.repository.ArticleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service article.
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;
    private final TopicRepository topicRepository;
    private final UserService userService;

    /**
     * Récupère tous les articles.
     *
     * @param sortBy le champ de tri
     * @param order l'ordre de tri
     * @return tous les articles sous forme de liste
     */
    @Override
    public List<Article> getAllArticles(String sortBy, String order) {
        // Définir les valeurs par défaut pour les paramètres de tri.
        if (sortBy == null) {
            sortBy = "id";
        }
        if (order == null) {
            order = "asc";
        }

        // Valider et convertir la direction de tri.
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(order.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order parameter: " + order);
        }

        // Valider le champ de tri.
        if (!isValidSortByField(sortBy, Article.class)) {
            throw new IllegalArgumentException("Invalid sortBy parameter: " + sortBy);
        }

        // Créer l'objet de tri et récupérer les articles triés.
        Sort sort = Sort.by(sortDirection, sortBy);
        return articleRepository.findAll(sort);
    }

    // Méthode privée pour valider le champ de tri.
    private boolean isValidSortByField(String sortBy, Class<?> clazz) {
        // Permet de filtrer sur chacun des champs de la classe Article.
        return Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(field -> field.getName().equals(sortBy));
    }

    /**
     * Récupère un article par son identifiant.
     *
     * @param id l'identifiant de l'article
     * @return l'article correspondant
     */
    @Override
    public Optional<Article> getArticleById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            throw new NotFoundException("Article not found with id: " + id);
        }
        return article;
    }

    /**
     * Récupère les articles par l'id de leur thème.
     *
     * @param topicId l'identifiant du thème des articles
     * @return la liste des articles qui ont pour thème le thème correspondant
     */
    @Override
    public List<Article> getArticlesByTopicId(Long topicId) {
        Optional<Topic> topic = topicRepository.findById(topicId);
        if(topic.isEmpty()) {
            throw new NotFoundException("Topic not found with id: " + topicId);
        }
        return articleRepository.findByTopicId(topicId);
    }

    /**
     * Sauvegarde l'article en base de données (utilisé pour une création ou une mise à jour).
     *
     * @param article l'article à sauvegarder
     * @return l'article sauvegardé
     */
    @Override
    public Article createArticle(Article article, String emailJwt) {
        // Vérifie si l'utilisateur contenu dans le jwt existe.
        User user = userService.getUserByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        // Vérifie si l'utilisateur est autorisé à créer un article pour un autre utilisateur.
        if(!user.equals(article.getAuthor())) {
            throw new ForbiddenException("You are not allowed to create an article for another user");
        }

        // Crée l'article et le renvoie vers le controller
        return articleRepository.save(article);
    }
}