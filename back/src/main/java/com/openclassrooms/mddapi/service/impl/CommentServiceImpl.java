package com.openclassrooms.mddapi.service.impl;


import com.openclassrooms.mddapi.exception.ForbiddenException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service commentaire.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    /**
     * Récupère un commentaire par son identifiant.
     *
     * @param id l'identifiant du commentaire
     * @return le commentaire correspondant
     */
    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    /**
     * Récupère la liste des commentaires par l'id de leur article.
     *
     * @param articleId l'identifiant de l'article des commentaires
     * @return la liste des commentaires qui ont pour article l'article correspondant
     */
    @Override
    public List<Comment> getCommentsByArticleId(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        if(article.isEmpty()) {
            throw new NotFoundException("Article not found with id: " + articleId);
        }
        return commentRepository.findByArticleId(articleId);
    }

    /**
     * Créé un nouveau article
     *
     * @param comment le commentaire à sauvegarder
     * @return le commentaire sauvegardé
     */
    @Override
    public Comment createComment(Comment comment, String emailJwt) {
        // Vérifie si l'utilisateur contenu dans le jwt existe.
        User user = userRepository.findByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        // Vérifie si l'utilisateur est autorisé à créer un article pour un autre utilisateur.
        if(!user.equals(comment.getUser())) {
            throw new ForbiddenException("You are not allowed to create a comment for another user");
        }

        return commentRepository.save(comment);
    }
}