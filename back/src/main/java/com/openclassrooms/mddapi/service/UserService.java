package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les utilisateurs.
 */
public interface UserService {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);
    User saveUser(User user);
    Optional<User> getUserByEmailOrUsername(String emailOrUsername);
    List<Topic> getUserSubscribedTopics(Long userId);
}
