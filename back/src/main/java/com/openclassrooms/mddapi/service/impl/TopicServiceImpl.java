package com.openclassrooms.mddapi.service.impl;

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
    public Topic subscribeUserToTopic(Long topicId, Long userId) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        // Gère le cas où le thème ou l'utilisateur n'existe pas
        if(topic == null || user == null) {
            throw new NotFoundException("User or topic not found");
        }

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
    public Topic unsubscribeUserFromTopic(Long topicId, Long userId) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        // Gère le cas où le thème ou l'utilisateur n'existe pas
        if(topic == null || user == null) {
            throw new NotFoundException("User or topic not found");
        }

        // Gère le cas où l'utilisateur n'est pas abonné au thème
        boolean alreadySubscribed = topic.getUsers_subscribed().stream().anyMatch(o -> o.getId().equals(userId));
        if(!alreadySubscribed) {
            throw new NotFoundException("User is not subscribed to topic");
        }

        // Retire l'utilisateur de la liste des abonnés
        topic.setUsers_subscribed(topic.getUsers_subscribed().stream().filter(o -> !o.getId().equals(userId)).collect(Collectors.toList()));

        return topicRepository.save(topic);
    }
}
