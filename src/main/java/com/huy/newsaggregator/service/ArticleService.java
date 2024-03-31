package com.huy.newsaggregator.service;

import com.huy.newsaggregator.dto.CreateArticleRequest;
import com.huy.newsaggregator.model.Article;
import com.huy.newsaggregator.model.Tag;
import com.huy.newsaggregator.repository.ArticleRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.huy.newsaggregator.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final TagService tagService;

    private final TagRepository tagRepository;

    public ArticleService(ArticleRepository articleRepository, TagService tagService, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagService = tagService;
        this.tagRepository = tagRepository;
    }

    public Article createArticle(CreateArticleRequest req) {
        Article article = new Article();
        article.setArticleLink(req.getArticleLink());
        article.setArticleTitle(req.getArticleTitle());
        article.setArticleSummary(req.getArticleSummary());
        article.setArticleType(req.getArticleType());
        article.setDetailedArticleContent(req.getDetailedArticleContent());
        article.setCreationDate(req.getCreationDate());
        article.setWebsiteResource(req.getWebsiteResource());
        article.setAuthorName(req.getAuthorName());

        Set<Tag> createTags = tagService.createTags(req.getHashtags());
        article.setHashtags(createTags);

        return articleRepository.save(article);
    }

    public List<Article> getAllArticle() {
        return articleRepository.findAll();
    }

    public List<Article> getArticleByResource(
        String resource, int pageNumber, int pageSize, String sortBy, String direction) {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        return articleRepository.findByWebsiteResourceContainingIgnoreCase(resource, pageable);
    }

    public List<Article> getArticleByKeyWord(
            String keyWord, int pageNumber, int pageSize, String sortBy, String direction) {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        return articleRepository.findByKeyWord(keyWord, pageable);
    }

    public List<Article> getArticleByType(
            String type, int pageNumber, int pageSize, String sortBy, String direction) {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        return articleRepository.findByArticleTypeContainingIgnoreCase(type, pageable);
    }

    public List<Article> getArticleByDate(
            LocalDate startDate, LocalDate endDate,
            int pageNumber, int pageSize, String direction) {
        Pageable pageable = createPageable(pageNumber, pageSize, "creationDate", direction);
        return articleRepository.findByCreationDateBetween(startDate, endDate, pageable);
    }

    private Pageable createPageable(int pageNumber, int pageSize, String sortBy, String direction) {
        Sort sort;
        if (direction.equalsIgnoreCase("DESC")) {
            sort = Sort.by(Sort.Direction.DESC, sortBy);
        } else {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        }
        return (Pageable) PageRequest.of(pageNumber, pageSize, sort);
    }

    public List<Article> getArticleByTag(String tag, int pageNumber, int pageSize, String sortBy,
                                         String direction) throws Exception {
        Optional<Tag> temp = tagRepository.findByName(tag);
        if (temp.isEmpty()) {
            throw new Exception("Tag does not exist ...");
        }
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        return articleRepository.findArticleByHashtagsId(temp.get().getId(), pageable);
    }

    public List<Long> findSuggestArticles(Long sourceArticleId) throws Exception {
        Optional<Article> sourceArticle = articleRepository.findById(sourceArticleId);
        if (sourceArticle.isEmpty()) {
            throw new Exception("article does not exist ...");
        }
        Article source = sourceArticle.get();
        List<Long> articleTagsIds = source.getHashtags().stream().map(Tag::getId).toList();

        Set<Long> similarArticlesIds = articleRepository.findArticleIdByTagId(articleTagsIds);

        similarArticlesIds.remove(source.getId());

        return similarArticlesIds.stream().toList();
    }

    public Article findArticleById(Long id) throws Exception {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            throw new Exception("Article does not exist ...");
        } else {
            return article.get();
        }
    }
}
