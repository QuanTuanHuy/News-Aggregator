package com.huy.newsaggregator.service;

import com.huy.newsaggregator.model.Tag;
import com.huy.newsaggregator.repository.ArticleRepository;
import com.huy.newsaggregator.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

//    public List<Tag> getTrendingTags() {
//        List<Long> tagIds = tagRepository.findTrendingTagIds();
//        List<Tag> tags = new ArrayList<>();
//        for (Long id : tagIds) {
//            tags.add(tagRepository.findById(id).get());
//        }
//        return tags;
//    }

    public List<Tag> getTrendingTagsByDate(LocalDate start, LocalDate end) {
        List<Long> tagIds = tagRepository.findTrendingTagIdsByDate(start, end);
        List<Tag> tags = new ArrayList<>();
        for (Long id : tagIds) {
            tags.add(tagRepository.findById(id).get());
        }
        return tags;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
