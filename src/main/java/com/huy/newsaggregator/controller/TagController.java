package com.huy.newsaggregator.controller;

import com.huy.newsaggregator.model.Tag;
import com.huy.newsaggregator.service.Imp.TagService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) throws Exception {
        Tag tag = tagService.findTagById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("/trending")
    public ResponseEntity<List<Tag>> trendingTagByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate start,
            @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate end) {
        List<Tag> trendingTags = tagService.getTrendingTagsByDate(start, end);
        return new ResponseEntity<>(trendingTags, HttpStatus.OK);
    }
}
