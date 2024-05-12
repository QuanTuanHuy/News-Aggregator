package com.huy.newsaggregator.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    private String articleLink;
    private String articleType;
    @Column(length = 2000)
    private String articleSummary;
    private String articleTitle;
    @Column(length = 13000)
    private String detailedArticleContent;
    private LocalDate creationDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_tag",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> hashtags;
    private String authorName;

    public void addTag(Tag tag) {
        this.hashtags.add(tag);
    }

    public void removeTag(Long tagId) {
        Tag tag = this.hashtags.stream().filter(
                x -> Objects.equals(x.getId(), tagId)).findFirst().orElse(null);
        if (tag != null) {
            this.hashtags.remove(tag);
            tag.getArticles().remove(this);
        }
    }

    public Article() {}

    public Article(String articleLink,
                   String articleType,
                   String articleSummary,
                   String articleTitle,
                   String detailedArticleContent,
                   LocalDate creationDate,
                   String authorName) {
        this.articleLink = articleLink;
        this.articleType = articleType;
        this.articleSummary = articleSummary;
        this.articleTitle = articleTitle;
        this.detailedArticleContent = detailedArticleContent;
        this.creationDate = creationDate;
        this.authorName = authorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Tag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<Tag> hashtags) {
        this.hashtags = hashtags;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }
}
