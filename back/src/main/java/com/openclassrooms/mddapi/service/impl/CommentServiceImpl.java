package com.openclassrooms.mddapi.service.impl;


import com.openclassrooms.mddapi.model.Comment;
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
        return commentRepository.findByArticleId(articleId);
    }

    /**
     * Sauvegarde le commentaire en base de données (utilisé pour une création ou une mise à jour).
     *
     * @param comment le commentaire à sauvegarder
     * @return le commentaire sauvegardé
     */
    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}