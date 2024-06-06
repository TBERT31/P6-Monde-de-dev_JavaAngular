package com.openclassrooms.mddapi.service.impl;


import com.openclassrooms.mddapi.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> getCommentsByArticleId(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}