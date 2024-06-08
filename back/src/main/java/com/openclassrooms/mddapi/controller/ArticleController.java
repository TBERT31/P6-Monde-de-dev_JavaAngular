package com.openclassrooms.mddapi.controller;

//import io.swagger.v3.oas.annotations.tags.Tag;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.mapper.ArticleMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@Tag(name = "Articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @GetMapping("")
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articleMapper.toDto(articles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(
            @PathVariable Long id
    ) {
        try {
            Optional<Article> optionalArticle = articleService.getArticleById(id);

            if (optionalArticle == null) {
                return ResponseEntity.notFound().build();
            }

            Article article = optionalArticle.get();

            return ResponseEntity.ok().body(articleMapper.toDto(article));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<ArticleDto>> getArticleByTopicId(
            @PathVariable Long topicId
    ) {
        List<Article> articles = articleService.getArticlesByTopicId(topicId);
        return ResponseEntity.ok(articleMapper.toDto(articles));
    }

    @PostMapping("")
    public ResponseEntity<ArticleDto> createArticle(
            @Valid @RequestBody ArticleDto articleDto
    ) {
        Article article = articleMapper.toEntity(articleDto);
        article = articleService.saveArticle(article);
        return ResponseEntity.ok(articleMapper.toDto(article));
    }
}
