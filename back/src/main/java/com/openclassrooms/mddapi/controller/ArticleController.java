package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.exception.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.exception.ForbiddenException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.mapper.ArticleMapper;

import javax.validation.Valid;
import java.util.List;


/**
 * Contrôleur pour les opérations liées aux articles.
 */
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

    /**
     * Récupère tous les articles.
     * @param sortBy le champ par lequel trier les articles.
     * @param order l'ordre du tri.
     * @return une liste d'articles.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticleDto>> getAllArticles(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        try {
            // Appel au service pour obtenir les articles triés.
            List<ArticleDto> articleDtos = articleMapper.toDto(articleService.getAllArticles(sortBy, order));
            return ResponseEntity.ok(articleDtos);
        } catch (IllegalArgumentException e) {
            // En cas de paramètre de tri invalide, retourner une réponse d'erreur.
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère un article par son identifiant.
     * @param id l'identifiant de l'article.
     * @return l'article.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
        try {
            Article article = articleService.getArticleById(id)
                    .orElseThrow(() -> new NotFoundException("Article not found with id: " + id));
            return ResponseEntity.ok().body(articleMapper.toDto(article));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère les articles par l'identifiant du sujet.
     * @param topicId l'identifiant du sujet.
     * @return une liste d'articles.
     */
    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<ArticleDto>> getArticleByTopicId(
            @PathVariable Long topicId
    ) {
        try {
            List<Article> articles = articleService.getArticlesByTopicId(topicId);
            return ResponseEntity.ok(articleMapper.toDto(articles));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crée un nouvel article.
     * @param articleDto les données de l'article à créer.
     * @param token le jeton d'authentification de l'utilisateur.
     * @return l'article créé.
     */
    @PostMapping("")
    public ResponseEntity<ArticleDto> createArticle(
            @Valid @RequestBody ArticleDto articleDto,
            @RequestHeader("Authorization") String token
    ) {
        // Récupère l'utilisateur à partir du jeton d'authentification.
        String jwt = token.substring(7);
        String emailJwt = jwtUtils.getUserNameFromJwtToken(jwt);

        // Vérifie si l'auteur de l'article existe.
        userService.getUserByUsername(articleDto.getAuthor())
                //(A méditer, car cela fournit bcp d'information sur nos users...)
                //.orElseThrow(() -> new NotFoundException("Author not found with username: " + articleDto.getAuthor()));

                // Permet de duper le hacker qui se croit malin
                .orElseThrow(() -> new ForbiddenException("You are not allowed to create an article for another user"));

        // Crée l'article.
        Article article = articleService.createArticle(articleMapper.toEntity(articleDto), emailJwt);
        return ResponseEntity.ok(articleMapper.toDto(article));
    }
}