package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
//import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.service.TopicService;
import com.openclassrooms.mddapi.mapper.TopicMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@Tag(name = "Topics")
public class TopicController {

    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @GetMapping("")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topicMapper.toDto(topics));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable Long id) {
        try {
            Optional<Topic> optionalTopic = topicService.getTopicById(id);

            if (optionalTopic == null) {
                return ResponseEntity.notFound().build();
            }

            Topic topic = optionalTopic.get();

            return ResponseEntity.ok().body(topicMapper.toDto(topic));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("{id}/subscribe/{userId}")
    public ResponseEntity<TopicDto> subscribeUserToTopic(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId
    ) {
        try {
            Topic topic = topicService.subscribeUserToTopic(id, userId);

            return ResponseEntity.ok().body(topicMapper.toDto(topic));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}/subscribe/{userId}")
    public ResponseEntity<TopicDto> unsubscribeUserFromTopic(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId
    ) {
        try {
            Topic topic = topicService.unsubscribeUserFromTopic(id, userId);

            return ResponseEntity.ok().body(topicMapper.toDto(topic));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
