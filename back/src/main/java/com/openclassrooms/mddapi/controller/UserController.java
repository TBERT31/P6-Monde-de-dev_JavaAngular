package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.exception.ForbiddenException;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ){
        String jwt = token.substring(7);
        String email = jwtUtils.getUserNameFromJwtToken(jwt);

        User authUser = userService.getUserByEmail(email).get();

        if (authUser.getId() != id.longValue()) {
            //throw new ForbiddenException("You are not allowed to access this user's information");
            return ResponseEntity.badRequest().build();
        }

        Optional<User> optionalUser = userService.getUserById(id.longValue());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = userMapper.toDto(user);

            return ResponseEntity.ok(userDto);
        } else {
            //return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable String email,
            @RequestHeader("Authorization") String token
    ){
        String jwt = token.substring(7);
        String jwtEmail = jwtUtils.getUserNameFromJwtToken(jwt);

        User authUser = userService.getUserByEmail(jwtEmail).get();

        Optional<User> optionalUser = userService.getUserByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (authUser.getId() != user.getId()) {
                //throw new ForbiddenException("You are not allowed to access this user's information");
                return ResponseEntity.badRequest().build();
            }

            UserDto userDto = userMapper.toDto(user);

            return ResponseEntity.ok(userDto);
        } else {
            //return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JwtResponse> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserDto userDto,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        String email = jwtUtils.getUserNameFromJwtToken(jwt);

        User authUser = userService.getUserByEmail(email).orElse(null);

        if (authUser == null || authUser.getId() != id.longValue()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> optionalUser = userService.getUserById(id.longValue());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());

            User updatedUser = userService.saveUser(user);

            // Générer un nouveau JWT en utilisant l'authentification de l'utilisateur mis à jour
            UserDetailsImpl userDetails = new UserDetailsImpl(
                    updatedUser.getId(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail(),
                    updatedUser.getPassword()
            );

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String newJwt = jwtUtils.generateJwtToken(authentication);

            JwtResponse jwtResponse = new JwtResponse(newJwt,
                    updatedUser.getId(),
                    updatedUser.getEmail(),
                    updatedUser.getUsername());

            return ResponseEntity.ok(jwtResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/topics")
    public ResponseEntity<List<TopicDto>> getUserSubscribedTopics(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        String email = jwtUtils.getUserNameFromJwtToken(jwt);

        User authUser = userService.getUserByEmail(email).orElse(null);

        if (authUser == null || authUser.getId() != id.longValue()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Topic> topics = userService.getUserSubscribedTopics(id);
        List<TopicDto> topicDtos = topicMapper.toDto(topics);
        return ResponseEntity.ok(topicDtos);
    }
}
