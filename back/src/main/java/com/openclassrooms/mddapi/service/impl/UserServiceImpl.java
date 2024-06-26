package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.ForbiddenException;
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
    // Utile pour les mappers et la sécurité
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Récupère un utilisateur par son identifiant, en checkant ces accès.
     *
     * @param id l'identifiant de l'utilisateur
     * @param emailJwt l'email de l'utilisateur authentifié
     * @return l'utilisateur correspondant
     */
    @Override
    public Optional<User> getUserByIdWithAuthorization(Long id, String emailJwt) {
        // Récupère l'utilisateur authentifié.
        User authUser = userRepository.findByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        // Vérifie si l'utilisateur authentifié est autorisé à accéder aux informations de l'utilisateur demandé.
        if (authUser.getId() != id.longValue()) {
            //throw new ForbiddenException("You are not allowed to access this user's information");
            throw new BadRequestException();
        }

        return userRepository.findById(id);
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email l'email de l'utilisateur
     * @return l'utilisateur correspondant
     */
    @Override
    // Utile pour les mappers et la sécurité
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Récupère un utilisateur par son email, en checkant ces accès
     *
     * @param email l'email de l'utilisateur
     * @param emailJwt l'email de l'utilisateur authentifié
     * @return l'utilisateur correspondant
     */
    @Override
    public Optional<User> getUserByEmailWithAuthorization(String email, String emailJwt) {
        // Récupère l'utilisateur authentifié.
        User authUser = userRepository.findByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        User searchedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        // Vérifie si l'utilisateur authentifié est autorisé à accéder aux informations de l'utilisateur demandé.
        if (!authUser.getId().equals(searchedUser.getId())) {
            //throw new ForbiddenException("You are not allowed to access this user's information");
            throw new BadRequestException();
        }

        return Optional.of(searchedUser);
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
     * Enregistre un utilisateur en base.
     *
     * @param user l'utilisateur à sauvegarder
     * @return l'utilisateur sauvegardé
     */
    @Override
    public User registerUser(User user){
        return userRepository.save(user);
    }

    /**
     * Mise à jour de l'utilisateur pour un utilisateur authentifié.
     *
     * @param id l'id de l'utilisateur
     * @param user l'utilisateur à sauvegarder
     * @param emailJwt l'email de l'utilisateur authentifié
     *
     * @return l'utilisateur sauvegardé
     */
    @Override
    public User updateUserById(Long id, User user, String emailJwt) {
        // Récupère l'utilisateur authentifié.
        User authUser = userRepository.findByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        // Vérifie si l'utilisateur authentifié est autorisé à mettre à jour les informations de l'utilisateur demandé.
        if (authUser == null || !authUser.getId().equals(id)) {
            throw new ForbiddenException("You are not allowed to update this user's information");
        }

        // Récupère l'utilisateur par son identifiant.
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new ForbiddenException("You are not allowed to update this user's information"));

        // Met à jour les informations de l'utilisateur.
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());

        // Sauvegarde les modifications.
        return userRepository.save(updatedUser);
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
    public List<Topic> getUserSubscribedTopics(Long userId, String emailJwt) {

        // Récupère l'utilisateur authentifié.
        User authUser = userRepository.findByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        // Vérifie si l'utilisateur authentifié est autorisé à accéder aux sujets auxquels l'utilisateur demandé est abonné.
        if (authUser == null || authUser.getId() != userId.longValue()) {
            throw new ForbiddenException("You are not allowed to access this user's information");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return user.getTopics_subscribed();
    }

}