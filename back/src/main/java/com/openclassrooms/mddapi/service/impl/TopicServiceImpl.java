package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService{

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;


    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @Override
    public Optional<Topic> getTopicById(Long topic_id) {
        return topicRepository.findById(topic_id);
    }

    @Override
    public void subscribeUserToTopic(Long topicId, Long userId) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if(topic == null || user == null) {
            throw new NotFoundException();
        }

        boolean alreadySubscribed = topic.getUsers_subscribed().stream().anyMatch(o -> o.getId().equals(userId));
        if(alreadySubscribed) {
            throw new BadRequestException();
        }

        topic.getUsers_subscribed().add(user);

        this.topicRepository.save(topic);
    }

    @Override
    public void unsubscribeUserFromTopic(Long topicId, Long userId) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if(topic == null) {
            throw new NotFoundException();
        }

        boolean alreadySubscribed = topic.getUsers_subscribed().stream().anyMatch(o -> o.getId().equals(userId));
        if(!alreadySubscribed) {
            throw new BadRequestException();
        }

        topic.setUsers_subscribed(topic.getUsers_subscribed().stream().filter(user -> !user.getId().equals(userId)).collect(Collectors.toList()));

        this.topicRepository.save(topic);
    }
}
