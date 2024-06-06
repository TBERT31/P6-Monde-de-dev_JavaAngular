package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    User saveUser(User user);
    Optional<User> getUserByEmailOrUsername(String email, String username);
}
