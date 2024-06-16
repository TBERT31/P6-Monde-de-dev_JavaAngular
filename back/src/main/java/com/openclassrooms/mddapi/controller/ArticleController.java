package com.openclassrooms.mddapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.exception.ForbiddenException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<ArticleDto>> getAllArticles(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        if (sortBy == null) {
            sortBy = "id";
        }

        if (order == null) {
            order = "asc";
        }

        return ResponseEntity.ok(
                articleMapper.toDto(articleService.getAllArticles(sortBy, order))
        );
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
            @Valid @RequestBody ArticleDto articleDto,
            @RequestHeader("Authorization") String token
    ) {

        String jwt = token.substring(7);
        String email = jwtUtils.getUserNameFromJwtToken(jwt);

        User user = userService.getUserByEmail(email).get();

        if(user.getUsername() != articleDto.getAuthor()) {
            throw new ForbiddenException("You are not allowed to create an article for another user");
        }

        Article article = articleMapper.toEntity(articleDto);
        article = articleService.saveArticle(article);
        return ResponseEntity.ok(articleMapper.toDto(article));
    }
}
