package com.huy.newsaggregator.controller;

import com.huy.newsaggregator.dto.CreateArticleRequest;
import com.huy.newsaggregator.model.Article;
import com.huy.newsaggregator.model.Tag;
import com.huy.newsaggregator.service.ArticleService;
import com.huy.newsaggregator.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/create")
    public ResponseEntity<Article> createArticle(@RequestBody CreateArticleRequest createArticleRequest) {
        Article createdArticle = articleService.createArticle(createArticleRequest);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @PostMapping("create/list")
    public ResponseEntity<List<Article>> createArticle(
            @RequestBody List<CreateArticleRequest> createArticleRequests) {
        List<Article> articles = articleService.createListArticle(createArticleRequests);
        return new ResponseEntity<>(articles, HttpStatus.CREATED);
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
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "15") Integer pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "creationDate") String sortBy,
            @RequestParam(name = "direct", required = false, defaultValue = "DESC") String direction) {
        List<Article> articles = articleService.getArticleByKeyWord(
                keyWord, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<List<Article>> getArticleByTag(
            @RequestParam String tag,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "15") Integer pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "creationDate") String sortBy,
            @RequestParam(name = "direct", required = false, defaultValue = "DESC") String direction) throws Exception {
        List<Article> articles = articleService.getArticleByTag(
                tag, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/resource")
    public ResponseEntity<List<Article>> getArticleByResource(
            @RequestParam String source,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "15") Integer pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "creationDate") String sortBy,
            @RequestParam(name = "direct", required = false, defaultValue = "DESC") String direction) {
        List<Article> articles = articleService.getArticleByResource(
                source, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<List<Article>> getArticleDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate start,
            @RequestParam  @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate end,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "15") Integer pageSize,
            @RequestParam(name = "direct", required = false, defaultValue = "DESC") String direction) {
        List<Article> articles = articleService.getArticleByDate(
                start, end, pageNumber, pageSize, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<Article>> getArticleType(
            @RequestParam String type,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "15") Integer pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "creationDate") String sortBy,
            @RequestParam(name = "direct", required = false, defaultValue = "DESC") String direction) {
        List<Article> articles = articleService.getArticleByType(
                type, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Article>> suggestArticles(@PathVariable Long id) throws Exception {
        List<Article> similarArticleIds = articleService.findSuggestArticles(id);
        return new ResponseEntity<>(similarArticleIds, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllArticles() {
        articleService.deleteAll();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
