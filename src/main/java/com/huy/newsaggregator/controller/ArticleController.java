package com.huy.newsaggregator.controller;

import com.huy.newsaggregator.dto.CreateArticleRequest;
import com.huy.newsaggregator.model.Article;
import com.huy.newsaggregator.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping()
    public ResponseEntity<Article> createArticle(@RequestBody CreateArticleRequest createArticleRequest) {
        Article createdArticle = articleService.createArticle(createArticleRequest);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Article>> getAllArticle() {
        List<Article> articles = articleService.getAllArticle();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) throws Exception {
        Article article = articleService.findArticleById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @GetMapping("/key")
    public ResponseEntity<List<Article>> getArticleByKeyWord(
            @RequestParam(value = "key") String keyWord,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
            @RequestParam(value = "sort", defaultValue = "creationDate") String sortBy,
            @RequestParam(value = "direct", defaultValue = "DESC") String direction) {
        List<Article> articles = articleService.getArticleByKeyWord(
                keyWord, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<List<Article>> getArticleByTag(
            @RequestParam String tag,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
            @RequestParam(value = "sort", defaultValue = "creationDate") String sortBy,
            @RequestParam(value = "direct", defaultValue = "DESC") String direction) throws Exception {
        List<Article> articles = articleService.getArticleByTag(
                tag, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/resource")
    public ResponseEntity<List<Article>> getArticleByResource(
            @RequestParam String source,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
            @RequestParam(value = "sort", defaultValue = "creationDate") String sortBy,
            @RequestParam(value = "direct", defaultValue = "DESC") String direction) {
        List<Article> articles = articleService.getArticleByResource(
                source, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<List<Article>> getArticleDate(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
            @RequestParam(value = "direct", defaultValue = "DESC") String direction) {
        List<Article> articles = articleService.getArticleByDate(
                start, end, pageNumber, pageSize, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<Article>> getArticleType(
            @RequestParam String type,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
            @RequestParam(value = "sort", defaultValue = "creationDate") String sortBy,
            @RequestParam(value = "direct", defaultValue = "DESC") String direction) {
        List<Article> articles = articleService.getArticleByType(
                type, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Long>> suggestArticles(@PathVariable Long id) throws Exception {
        List<Long> similarArticleIds = articleService.findSuggestArticles(id);
        return new ResponseEntity<>(similarArticleIds, HttpStatus.OK);
    }

}
