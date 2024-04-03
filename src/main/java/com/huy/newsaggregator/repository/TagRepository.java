package com.huy.newsaggregator.repository;

import com.huy.newsaggregator.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tagName);
    List<Tag> findTagByArticlesId(Long ArticleId);

//    @Query("SELECT t.id FROM Tag t JOIN t.articles a GROUP BY t.id ORDER BY COUNT(t.id) DESC")
//    List<Long> findTrendingTagIds();

    @Query("SELECT t.id FROM Tag t JOIN t.articles a WHERE a.creationDate >= :start AND a.creationDate <= :end" +
            " GROUP BY t.id ORDER BY COUNT(t.id) DESC")
    List<Long> findTrendingTagIdsByDate(@Param("start") LocalDate start,@Param("end") LocalDate end);
}
