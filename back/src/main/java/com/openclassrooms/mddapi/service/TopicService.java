package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Topic;

import java.util.List;


public interface TopicService {
    List<Topic> getAllTopics();
    void subscribeUserToTopic(Long topic_id, Long user_id);
    void unsubscribeUserFromTopic(Long topic_id, Long user_id);
}
