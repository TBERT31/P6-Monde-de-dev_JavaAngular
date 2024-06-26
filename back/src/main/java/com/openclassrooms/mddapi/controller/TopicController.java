package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.service.TopicService;
import com.openclassrooms.mddapi.mapper.TopicMapper;

import java.util.List;
import java.util.Optional;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.service.UserService;

/**
 * Contrôleur pour les opérations liées aux sujets.
 */
@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Tag(name = "Topics")
public class TopicController {

    private final TopicService topicService;
    private final TopicMapper topicMapper;
    private final JwtUtils jwtUtils;

    /**
     * Récupère tous les sujets.
     * @return une liste de sujets.
     */
    @GetMapping("")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topicMapper.toDto(topics));
    }

    /**
     * Récupère un sujet par son identifiant.
     * @param id l'identifiant du sujet.
     * @return le sujet.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getTopicById(
            @PathVariable Long id
    ) {
        try {
            // Récupère le sujet par son identifiant.
            Optional<Topic> optionalTopic = topicService.getTopicById(id);

            // Vérifie si le sujet existe.
            if (optionalTopic.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Récupère le sujet.
            Topic topic = optionalTopic.get();

            return ResponseEntity.ok().body(topicMapper.toDto(topic));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Abonne un utilisateur à un sujet.
     * @param id l'identifiant du sujet.
     * @param userId l'identifiant de l'utilisateur.
     * @param token le jeton d'authentification de l'utilisateur.
     * @return le sujet.
     */
    @PostMapping("{id}/subscribe/{userId}")
    public ResponseEntity<TopicDto> subscribeUserToTopic(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            // Récupère l'utilisateur à partir du jeton d'authentification.
            String jwt = token.substring(7);
            String emailJwt = jwtUtils.getUserNameFromJwtToken(jwt);

            // Abonne l'utilisateur au sujet.
            Topic topic = topicService.subscribeUserToTopic(id, userId, emailJwt);

            return ResponseEntity.ok().body(topicMapper.toDto(topic));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Désabonne un utilisateur d'un sujet.
     * @param id l'identifiant du sujet.
     * @param userId l'identifiant de l'utilisateur.
     * @param token le jeton d'authentification de l'utilisateur.
     * @return le sujet.
     */
    @DeleteMapping("{id}/subscribe/{userId}")
    public ResponseEntity<TopicDto> unsubscribeUserFromTopic(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            // Récupère l'utilisateur à partir du jeton d'authentification.
            String jwt = token.substring(7);
            String emailJwt = jwtUtils.getUserNameFromJwtToken(jwt);

            // Désabonne l'utilisateur du sujet.
            Topic topic = topicService.unsubscribeUserFromTopic(id, userId, emailJwt);

            return ResponseEntity.ok().body(topicMapper.toDto(topic));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
