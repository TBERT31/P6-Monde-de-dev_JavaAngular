package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.exception.ForbiddenException;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.model.User;
//import io.swagger.v3.oas.annotations.tags.Tag;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

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
}
