package com.huy.newsaggregator.service;

import com.huy.newsaggregator.model.Tag;
import com.huy.newsaggregator.repository.ArticleRepository;
import com.huy.newsaggregator.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    public TagService(TagRepository tagRepository, ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
    }

    public Tag createTag(String tag) {
        Tag createdTag = new Tag();
        createdTag.setName(tag);
        return tagRepository.save(createdTag);
    }

    public Tag getOrCreateTag(String tag) {
        Optional<Tag> temp = tagRepository.findByName(tag);
        return temp.orElseGet(() -> createTag(tag));
    }

    public Set<Tag> createTags(Set<String> hashtags) {
        Set<Tag> tags = new HashSet<>();
        for (String tag : hashtags) {
            tags.add(getOrCreateTag(tag));
        }
        return tags;
    }
}
