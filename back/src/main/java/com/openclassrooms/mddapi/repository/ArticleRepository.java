package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAll(Sort sort);
    List<Article> findByTopicId(Long topicId);
}
