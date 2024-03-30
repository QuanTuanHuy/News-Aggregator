package com.huy.newsaggregator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Set;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String articleLink;
    private String websiteResource;
    private String articleType;
    private String articleSummary;
    private String articleTitle;
    private String detailedArticleContent;
    private LocalDate creationDate;
    private Set<String> hashtags;
    private String authorName;

    public Article() {}

    public Article(Long id,
                   String articleLink,
                   String websiteResource,
                   String articleType,
                   String articleSummary,
                   String articleTitle,
                   String detailedArticleContent,
                   LocalDate creationDate,
                   Set<String> hashtags,
                   String authorName) {
        this.id = id;
        this.articleLink = articleLink;
        this.websiteResource = websiteResource;
        this.articleType = articleType;
        this.articleSummary = articleSummary;
        this.articleTitle = articleTitle;
        this.detailedArticleContent = detailedArticleContent;
        this.creationDate = creationDate;
        this.hashtags = hashtags;
        this.authorName = authorName;
    }
    public Article(String articleLink,
                   String websiteResource,
                   String articleType,
                   String articleSummary,
                   String articleTitle,
                   String detailedArticleContent,
                   LocalDate creationDate,
                   Set<String> hashtags,
                   String authorName) {
        this.articleLink = articleLink;
        this.websiteResource = websiteResource;
        this.articleType = articleType;
        this.articleSummary = articleSummary;
        this.articleTitle = articleTitle;
        this.detailedArticleContent = detailedArticleContent;
        this.creationDate = creationDate;
        this.hashtags = hashtags;
        this.authorName = authorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getWebsiteResource() {
        return websiteResource;
    }

    public void setWebsiteResource(String websiteResource) {
        this.websiteResource = websiteResource;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getArticleSummary() {
        return articleSummary;
    }

    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getDetailedArticleContent() {
        return detailedArticleContent;
    }

    public void setDetailedArticleContent(String detailedArticleContent) {
        this.detailedArticleContent = detailedArticleContent;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
