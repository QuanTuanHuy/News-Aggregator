package com.huy.newsaggregator.service;

import com.huy.newsaggregator.model.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ITagService {
    Tag createTag(String tag);
    Tag getOrCreateTag(String tag);
    Set<Tag> createTags(Set<String> hashtags);
    List<Tag> getTrendingTagsByDate(LocalDate start, LocalDate end);
    List<Tag> getAllTags();
    Tag findTagById(Long id) throws Exception;
    Tag getTagByName(String name) throws Exception;

}
