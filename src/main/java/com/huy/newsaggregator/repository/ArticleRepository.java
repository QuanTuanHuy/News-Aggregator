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

//    @Query("SELECT a.id FROM Article a JOIN a.hashtags t WHERE t.id = :tagId")
//    Set<Long> findArticleIdByTagId(@Param("tagId") Long tagId);

    @Query("SELECT a.id FROM Article a JOIN a.hashtags t WHERE t.id IN :tagIds")
    Set<Long> findArticleIdByTagId(@Param("tagIds") List<Long> tagIds);

//    @Query("SELECT COUNT(DISTINCT t) " +
//            "FROM Post p1 JOIN p1.tags t " +
//            "WHERE p1.id = :postId1 " +
//            "AND EXISTS " +
//            "(SELECT 1 FROM Post p2 JOIN p2.tags t2 WHERE p2.id = :postId2 AND t2 = t)")
//    int countSimilarTags(@Param("id1") Long articleId1, @Param("id2") Long articleId2);

//    @Query("SELECT COUNT(DISTINCT t) FROM Article a2 JOIN a2.hashtags t " +
//            "WHERE a2.id = :id2 " +
//            "AND t.id IN " +
//            "(SELECT a.hashtags FROM Article a WHERE a.id = :id1")
//    int countSimilarTags(@Param("id1") Long articleId1, @Param("id2") Long articleId2);
}
