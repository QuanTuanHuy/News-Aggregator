package com.huy.newsaggregator.repository;

import com.huy.newsaggregator.model.Article;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    List<Article> findByWebsiteResourceContainingIgnoreCase(String websiteResource, Pageable pageable);

    List<Article> findByArticleTypeContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE lower(a.articleTitle) LIKE lower(concat('%', :keyWord, '%')) " +
            "OR lower(a.detailedArticleContent) LIKE lower(concat('%', :keyWord, '%')) " +
            "OR lower(a.articleSummary) LIKE lower(concat('%', :keyWord, '%'))")
    List<Article> findByKeyWord(String keyWord, Pageable pageable);

    List<Article> findByCreationDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<Article> findArticleByHashtagsId(Long tagId, Pageable pageable);

    @Query("SELECT a.id FROM Article a JOIN a.hashtags t WHERE t.id IN :tagIds " +
            "GROUP BY a.id order by COUNT(a.id) DESC LIMIT 5")
    List<Long> findSimilarArticleIdByTagId(@Param("tagIds") List<Long> tagIds);
}
