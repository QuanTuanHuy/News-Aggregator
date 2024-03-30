package com.huy.newsaggregator.service;

import com.huy.newsaggregator.dto.CreateArticleRequest;
import com.huy.newsaggregator.model.Article;
import com.huy.newsaggregator.repository.ArticleRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article createArticle(CreateArticleRequest req) {
        Article article = new Article();
        article.setArticleLink(req.getArticleLink());
        article.setArticleTitle(req.getArticleTitle());
        article.setArticleSummary(req.getArticleSummary());
        article.setArticleType(req.getArticleType());
        article.setDetailedArticleContent(req.getDetailedArticleContent());
        article.setCreationDate(req.getCreationDate());
        article.setHashtags(req.getHashtags());
        article.setWebsiteResource(req.getWebsiteResource());
        article.setAuthorName(req.getAuthorName());

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

//    public List<Article> getArticleByTag(String tag, Pageable pageable) {
//        return articleRepository.findByTag(tag, pageable);
//    }
}
