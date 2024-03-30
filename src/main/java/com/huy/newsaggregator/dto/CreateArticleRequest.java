package com.huy.newsaggregator.dto;

import java.time.LocalDate;
import java.util.Set;

public class CreateArticleRequest {
    private String articleLink;
    private String websiteResource;
    private String articleType;
    private String articleSummary;
    private String articleTitle;
    private String detailedArticleContent;
    private LocalDate creationDate;
    private Set<String> hashtags;
    private String authorName;

    public CreateArticleRequest() {}

    public CreateArticleRequest(String articleLink,
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
