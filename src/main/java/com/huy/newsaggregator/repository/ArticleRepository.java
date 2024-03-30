package com.huy.newsaggregator.repository;

import com.huy.newsaggregator.model.Article;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    List<Article> findByWebsiteResourceContainingIgnoreCase(String websiteResource, Pageable pageable);

    List<Article> findByArticleTypeContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE lower(a.articleTitle) LIKE lower(concat('%', :keyWord, '%')) " +
            "OR lower(a.detailedArticleContent) LIKE lower(concat('%', :keyWord, '%')) " +
            "OR lower(a.articleSummary) LIKE lower(concat('%', :keyWord, '%'))")
    List<Article> findByKeyWord(String keyWord, Pageable pageable);

    List<Article> findByCreationDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);


//    @Query("SELECT a from Article a WHERE a.hashtags CONTAINS :tag")
//    List<Article> findByTag(@Param("tag") String tag, Pageable pageable);
}