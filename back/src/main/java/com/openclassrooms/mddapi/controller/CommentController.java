package com.openclassrooms.mddapi.controller;

//import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.model.Comment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@Tag(name = "Comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        try {
            Optional<Comment> optionalComment = commentService.getCommentById(id);

            if (optionalComment == null) {
                return ResponseEntity.notFound().build();
            }

            Comment comment = optionalComment.get();

            return ResponseEntity.ok().body(commentMapper.toDto(comment));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentDto>> getCommentByArticleId(
            @PathVariable Long articleId
    ) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(commentMapper.toDto(comments));
    }

    @PostMapping("")
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CommentDto commentDto//,
            //@RequestHeader("Authorization") String token
    ) {
        Comment comment = commentMapper.toEntity(commentDto);

        comment = commentService.saveComment(comment);

        return ResponseEntity.ok(commentMapper.toDto(comment));
    }

}
