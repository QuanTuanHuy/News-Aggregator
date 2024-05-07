package com.huy.newsaggregator.repository;

import com.huy.newsaggregator.model.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    //handle single field search

    List<Article> findByArticleType(String title, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE lower(a.articleTitle) LIKE lower(concat('%', :keyWord, '%')) " +
            "OR lower(a.detailedArticleContent) LIKE lower(concat('%', :keyWord, '%')) " +
            "OR lower(a.articleSummary) LIKE lower(concat('%', :keyWord, '%'))")
    List<Article> findByKeyWord(String keyWord, Pageable pageable);

    List<Article> findByCreationDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Article> findArticleByHashtagsId(Long tagId, Pageable pageable);

    Page<Article> findArticleByResourceId(Long resourceId, Pageable pageable);

    @Query("SELECT a.id FROM Article a JOIN a.hashtags t WHERE t.id IN :tagIds " +
            "GROUP BY a.id order by COUNT(a.id) DESC LIMIT 6")
    List<Long> findSimilarArticleIdByTagId(@Param("tagIds") List<Long> tagIds);


    // handle form search

    @Query("SELECT a.id FROM Article a ORDER BY a.id")
    List<Long> findAllArticleId();
    @Query("SELECT a.id FROM Article a JOIN a.resource r WHERE r.id = :resourceId")
    HashSet<Long> findArticleByResourceId(@Param("resourceId") Long resourceId);

    @Query("SELECT a.id FROM Article a WHERE a.articleType LIKE concat('%', :name, '%')")
    HashSet<Long> findByArticleType(@Param("name") String name);

    @Query("SELECT a.id FROM Article a WHERE lower(a.articleTitle) LIKE lower(concat('%', :keyWord, '%')) " +
            "OR lower(a.detailedArticleContent) LIKE lower(concat('%', :keyWord, '%')) " +
            "OR lower(a.articleSummary) LIKE lower(concat('%', :keyWord, '%'))")
    HashSet<Long> findByKeyWord(@Param("keyWord") String keyWord);

    @Query("SELECT a.id FROM Article a WHERE a.creationDate >= :startDate AND a.creationDate <= :endDate")
    HashSet<Long> findByCreationDateBetween(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    @Query("SELECT a.id FROM Article a JOIN a.hashtags t WHERE t.id = :tagId")
    HashSet<Long> findArticleByHashtagsId(@Param("tagId") Long tagId);

    @Query("SELECT a FROM Article a WHERE a.id IN :searchId")
    Page<Article> findAllByIdsIn(@Param("searchId") List<Long> searchId, Pageable pageable);
}
