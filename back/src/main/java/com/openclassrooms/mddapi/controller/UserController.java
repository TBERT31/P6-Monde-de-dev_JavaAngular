package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.payload.response.JwtResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour les opérations liées aux utilisateurs.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final TopicMapper topicMapper;

    // dans le cas des erreurs 403/404 sur ces routes sensibles,
    // nous préfèrerons rester vague afin d'éviter de dévoiler des informations sur les utilisateurs
    // présents dans la BDD.

    /**
     * Récupère un utilisateur par son identifiant.
     * @param id l'identifiant de l'utilisateur.
     * @param token le jeton d'authentification de l'utilisateur.
     * @return l'utilisateur.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ){
        // Récupère l'email de l'utilisateur authentifié.
        String jwt = token.substring(7);
        String email = jwtUtils.getUserNameFromJwtToken(jwt);

        // Récupère l'utilisateur authentifié.
        User authUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        // Vérifie si l'utilisateur authentifié est autorisé à accéder aux informations de l'utilisateur demandé.
        if (authUser.getId() != id.longValue()) {
            //throw new ForbiddenException("You are not allowed to access this user's information");
            return ResponseEntity.badRequest().build();
        }

        // Récupère l'utilisateur par son identifiant.
        Optional<User> optionalUser = userService.getUserById(id.longValue());

        // Vérifie si l'utilisateur existe.
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = userMapper.toDto(user);

            // Retourne l'utilisateur.
            return ResponseEntity.ok(userDto);
        } else {
            //return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère un utilisateur par son email.
     * @param email l'email de l'utilisateur.
     * @param token le jeton d'authentification de l'utilisateur.
     * @return l'utilisateur.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable String email,
            @RequestHeader("Authorization") String token
    ){
        // Récupère l'email de l'utilisateur authentifié.
        String jwt = token.substring(7);
        String jwtEmail = jwtUtils.getUserNameFromJwtToken(jwt);

        // Récupère l'utilisateur authentifié.
        User authUser = userService.getUserByEmail(jwtEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        Optional<User> optionalUser = userService.getUserByEmail(email);

        // Vérifie si l'utilisateur existe.
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Vérifie si l'utilisateur authentifié est autorisé à accéder aux informations de l'utilisateur demandé.
            if (!authUser.getId().equals(user.getId())) {
                //throw new ForbiddenException("You are not allowed to access this user's information");
                return ResponseEntity.badRequest().build();
            }

            UserDto userDto = userMapper.toDto(user);

            // Retourne l'utilisateur.
            return ResponseEntity.ok(userDto);
        } else {
            //return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un utilisateur.
     * @param id l'identifiant de l'utilisateur.
     * @param userDto les nouvelles données de l'utilisateur.
     * @param token le jeton d'authentification de l'utilisateur.
     * @return une réponse avec le nouveau jeton JWT si la mise à jour est réussie.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JwtResponse> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserDto userDto,
            @RequestHeader("Authorization") String token
    ) {
        // Récupère l'email de l'utilisateur authentifié.
        String jwt = token.substring(7);
        String email = jwtUtils.getUserNameFromJwtToken(jwt);

        // Récupère l'utilisateur authentifié.
        User authUser = userService.getUserByEmail(email).orElse(null);

        // Vérifie si l'utilisateur authentifié est autorisé à mettre à jour les informations de l'utilisateur demandé.
        if (authUser == null || authUser.getId() != id.longValue()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> optionalUser = userService.getUserById(id.longValue());

        // Vérifie si l'utilisateur existe.
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());

            // Met à jour l'utilisateur.
            User updatedUser = userService.saveUser(user);

            // Générer un nouveau JWT en utilisant l'authentification de l'utilisateur mis à jour
            UserDetailsImpl userDetails = new UserDetailsImpl(
                    updatedUser.getId(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail(),
                    updatedUser.getPassword()
            );

            // Authentifie l'utilisateur avec les informations mises à jour.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String newJwt = jwtUtils.generateJwtToken(authentication);

            // Retourne le nouveau jeton JWT et les informations de l'utilisateur.
            JwtResponse jwtResponse = new JwtResponse(newJwt,
                    updatedUser.getId(),
                    updatedUser.getEmail(),
                    updatedUser.getUsername());

            return ResponseEntity.ok(jwtResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Récupère les sujets auxquels un utilisateur est abonné.
     * @param id l'identifiant de l'utilisateur.
     * @param token le jeton d'authentification de l'utilisateur.
     * @return une liste de sujets.
     */
    @GetMapping("/{id}/topics")
    public ResponseEntity<List<TopicDto>> getUserSubscribedTopics(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        // Récupère l'email de l'utilisateur authentifié.
        String jwt = token.substring(7);
        String email = jwtUtils.getUserNameFromJwtToken(jwt);

        // Récupère l'utilisateur authentifié.
        User authUser = userService.getUserByEmail(email).orElse(null);

        // Vérifie si l'utilisateur authentifié est autorisé à accéder aux sujets auxquels l'utilisateur demandé est abonné.
        if (authUser == null || authUser.getId() != id.longValue()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Récupère et retourne les sujets auxquels l'utilisateur est abonné.
        List<Topic> topics = userService.getUserSubscribedTopics(id);
        List<TopicDto> topicDtos = topicMapper.toDto(topics);
        return ResponseEntity.ok(topicDtos);
    }
}
