package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Topic;

import java.util.List;
import java.util.Optional;


public interface TopicService {
    List<Topic> getAllTopics();
    Optional<Topic> getTopicById(Long topic_id);
    Optional<Topic> getTopicByTitle(String topic_title);
    Topic subscribeUserToTopic(Long topic_id, Long user_id);
    Topic unsubscribeUserFromTopic(Long topic_id, Long user_id);
}
