package com.huy.newsaggregator.repository;

import com.huy.newsaggregator.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tagName);
    List<Tag> findTagByArticlesId(Long ArticleId);
}
