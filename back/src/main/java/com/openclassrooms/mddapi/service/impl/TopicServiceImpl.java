package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.exception.ForbiddenException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service sujet/thème.
 */
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService{

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;


    /**
     * Récupère tous les thèmes.
     *
     * @return tous les thèmes sous forme de liste
     */
    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    /**
     * Récupère un thème par son identifiant.
     *
     * @param topic_id l'identifiant du thème
     * @return le thème correspondant
     */
    @Override
    public Optional<Topic> getTopicById(Long topic_id) {
        return topicRepository.findById(topic_id);
    }

    /**
     * Récupère un thème par son titre.
     *
     * @param topic_title le titre du thème
     * @return le thème correspondant
     */
    @Override
    // Utile pour les mappers
    public Optional<Topic> getTopicByTitle(String topic_title) {
        return topicRepository.findByTitle(topic_title);
    }

    /**
     * Abonne un utilisateur à un thème.
     *
     * @param topicId l'identifiant du thème
     * @param userId l'identifiant de l'utilisateur
     * @return le thème mis à jour
     */
    @Override
    public Topic subscribeUserToTopic(Long topicId, Long userId, String emailJwt){
        // Vérifie si l'utilisateur qui emet la requête existe.
        User userJwt = userRepository.findByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        // Vérifie si l'utilisateur est autorisé à s'abonner à la place d'un autre utilisateur.
        if (!userJwt.getId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to subscribe other users to topics");
        }

        // Récupère le thème
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found with id: " + topicId));

        // Récupère l'utilisateur
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ForbiddenException("You are not allowed to subscribe other users to topics"));

        // Gère le cas où l'utilisateur est déjà abonné au thème
        boolean alreadySubscribed = topic.getUsers_subscribed().stream().anyMatch(o -> o.getId().equals(userId));
        if(alreadySubscribed) {
            throw new DataIntegrityViolationException("User already subscribed to topic");
        }

        // Ajoute l'utilisateur à la liste des abonnés
        topic.getUsers_subscribed().add(user);

        return topicRepository.save(topic);
    }

    /**
     * Désabonne un utilisateur d'un thème.
     *
     * @param topicId l'identifiant du thème
     * @param userId l'identifiant de l'utilisateur
     * @return le thème mis à jour
     */
    @Override
    public Topic unsubscribeUserFromTopic(Long topicId, Long userId, String emailJwt){
        // Vérifie si l'utilisateur qui emet la requête existe.
        User userJwt = userRepository.findByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        // Vérifie si l'utilisateur est autorisé à se désabonner d'autres utilisateurs.
        if (!userJwt.getId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to unsubscribe other users to topics");
        }

        // Récupère le thème
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found with id: " + topicId));

        // Récupère l'utilisateur
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ForbiddenException("You are not allowed to subscribe other users to topics"));


        // Gère le cas où l'utilisateur n'est pas abonné au thème
        boolean alreadySubscribed = topic.getUsers_subscribed().stream().anyMatch(o -> o.getId().equals(userId));
        if(!alreadySubscribed) {
            throw new NotFoundException("User is not subscribed to topic");
        }

        // Retire l'utilisateur de la liste des abonnés
        topic.setUsers_subscribed(topic.getUsers_subscribed().stream().filter(o -> !o.getId().equals(user.getId())).collect(Collectors.toList()));

        return topicRepository.save(topic);
    }

    @Override
    public List<Topic> getTopicsByUserId(Long userId, String emailJwt) {
        // Récupère l'utilisateur authentifié.
        User authUser = userRepository.findByEmail(emailJwt)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + emailJwt));

        // Vérifie si l'utilisateur authentifié est autorisé à accéder aux sujets auxquels l'utilisateur demandé est abonné.
        if (authUser == null || authUser.getId() != userId.longValue()) {
            throw new ForbiddenException("You are not allowed to access this user's information");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ForbiddenException("You are not allowed to access this user's information"));

        return user.getTopics_subscribed();
    }
}
