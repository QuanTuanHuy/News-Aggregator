package com.huy.newsaggregator.service;

import com.huy.newsaggregator.dto.CreateArticleRequest;
import com.huy.newsaggregator.model.Article;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IArticleService {
    Article createArticle(CreateArticleRequest req) throws Exception;
    List<Article> createListArticle(List<CreateArticleRequest> requests) throws Exception;
    List<Article> getAllArticle();
    Map<String, Object> getArticleByResource(
            String resource, Integer pageNumber, Integer pageSize, String sortBy, String direction) throws Exception;
    List<Article> getArticleByKeyWord(
            String keyWord, Integer pageNumber, Integer pageSize, String sortBy, String direction);
    List<Article> getArticleByType(
            String type, Integer pageNumber, Integer pageSize, String sortBy, String direction);
    List<Article> getArticleByDate(
            LocalDate startDate, LocalDate endDate,
            int pageNumber, int pageSize, String direction);
    Map<String, Object> getArticleByTag(String tag, Integer pageNumber, Integer pageSize, String sortBy,
                                               String direction) throws Exception;
    List<Article> findSuggestArticles(Long sourceArticleId) throws Exception;
    Article getArticleById(Long id) throws Exception;
    void deleteAll();
    Map<String, Object> findArticleBySearchForm(
            String resource,
            String type,
            String keyWord,
            LocalDate startDate,
            LocalDate endDate,
            String tagName,
            String sortBy,
            Integer pageNumber,
            Integer pageSize,
            String direction) throws Exception;

}
