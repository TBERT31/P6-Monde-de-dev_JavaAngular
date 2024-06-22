package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service utilisateur.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return l'utilisateur correspondant
     */
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email l'email de l'utilisateur
     * @return l'utilisateur correspondant
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom de l'utilisateur
     * @return l'utilisateur correspondant
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Sauvegarde l'utilisateur en base de données (utilisé pour une création ou une mise à jour).
     *
     * @param user l'utilisateur à sauvegarder
     * @return l'utilisateur sauvegardé
     */
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Récupère un utilisateur par son email ou son nom d'utilisateur.
     *
     * @param emailOrUsername email ou nom d'utilisateur de l'utilisateur
     * @return l'utilisateur correspondant
     */
    @Override
    public Optional<User> getUserByEmailOrUsername(String emailOrUsername) {
        return userRepository.findByUsernameOrEmail(emailOrUsername, emailOrUsername);
    }

    /**
     * Récupère les sujets auxquels l'utilisateur est abonné.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste des sujets auxquels l'utilisateur est abonné
     */
    @Override
    public List<Topic> getUserSubscribedTopics(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return user.getTopics_subscribed();
    }

}
