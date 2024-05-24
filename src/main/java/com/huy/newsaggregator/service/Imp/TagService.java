package com.huy.newsaggregator.service.Imp;

import com.huy.newsaggregator.exception.ResourceNotFoundException;
import com.huy.newsaggregator.model.Tag;
import com.huy.newsaggregator.repository.TagRepository;
import com.huy.newsaggregator.service.ITagService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TagService implements ITagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(String tag) {
        Tag createdTag = new Tag();
        createdTag.setName(tag);
        return tagRepository.save(createdTag);
    }

    @Override
    public Tag getOrCreateTag(String tag) {
        Optional<Tag> temp = tagRepository.findByName(tag);
        return temp.orElseGet(() -> createTag(tag));
    }

    @Override
    public Set<Tag> createTags(Set<String> hashtags) {
        Set<Tag> tags = new HashSet<>();
        for (String tag : hashtags) {
            tags.add(getOrCreateTag(tag));
        }
        return tags;
    }

    @Override
    public List<Tag> getTrendingTagsByDate(LocalDate start, LocalDate end) {
        List<Long> tagIds = tagRepository.findTrendingTagIdsByDate(start, end);
        List<Tag> tags = new ArrayList<>();
        for (Long id : tagIds) {
            tags.add(tagRepository.findById(id).get());
        }
        return tags;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findTagById(Long id) throws Exception {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new ResourceNotFoundException("Tag", "ID", id);
        }
        return tag.get();
    }

    @Override
    public Tag getTagByName(String name) throws ResourceNotFoundException {
        Optional<Tag> temp = tagRepository.findByName(name);
        if (temp.isEmpty()) {
            throw new ResourceNotFoundException("Tag", "name", name);
        }
        return temp.get();
    }
}
