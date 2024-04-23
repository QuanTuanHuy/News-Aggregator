package com.huy.newsaggregator.service;

import com.huy.newsaggregator.dto.CreateArticleRequest;
import com.huy.newsaggregator.model.Article;
import com.huy.newsaggregator.model.Resource;
import com.huy.newsaggregator.model.Tag;
import com.huy.newsaggregator.repository.ArticleRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.huy.newsaggregator.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final TagService tagService;

    private final TagRepository tagRepository;

    private final ResourceService resourceService;

    public ArticleService(ArticleRepository articleRepository, TagService tagService, TagRepository tagRepository, ResourceService resourceService) {
        this.articleRepository = articleRepository;
        this.tagService = tagService;
        this.tagRepository = tagRepository;
        this.resourceService = resourceService;
    }

    public Article createArticle(CreateArticleRequest req) throws Exception {
        Article article = new Article();
        article.setArticleLink(req.getArticleLink());
        article.setArticleTitle(req.getArticleTitle());
        article.setArticleSummary(req.getArticleSummary());
        article.setArticleType(req.getArticleType());
        article.setDetailedArticleContent(req.getDetailedArticleContent());
        article.setCreationDate(req.getCreationDate());
        article.setAuthorName(req.getAuthorName());

        Resource resource = resourceService.getResourceByName(req.getWebsiteResource());
        article.setResource(resource);

        Set<Tag> createTags = tagService.createTags(req.getHashtags());
        article.setHashtags(createTags);

        return articleRepository.save(article);
    }

    public List<Article> createListArticle(List<CreateArticleRequest> reqs) throws Exception {
        List<Article> articles = new ArrayList<>();
        for (var req : reqs) {
            articles.add(createArticle(req));
        }
        return articles;
    }

    public List<Article> getAllArticle() {
        return articleRepository.findAll();
    }

    public List<Article> getArticleByResource(
        String resource, Integer pageNumber, Integer pageSize, String sortBy, String direction) throws Exception {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        Resource resource1 = resourceService.getResourceByName(resource);
        return articleRepository.findArticleByResourceId(resource1.getId(), pageable);
    }

    public List<Article> getArticleByKeyWord(
            String keyWord, Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        return articleRepository.findByKeyWord(keyWord, pageable);
    }

    public List<Article> getArticleByType(
            String type, Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        return articleRepository.findByArticleType(type, pageable);
    }

    public List<Article> getArticleByDate(
            LocalDate startDate, LocalDate endDate,
            int pageNumber, int pageSize, String direction) {
        Pageable pageable = createPageable(pageNumber, pageSize, "creationDate", direction);
        return articleRepository.findByCreationDateBetween(startDate, endDate, pageable);
    }

    private Pageable createPageable(Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        Sort sort;
        if (direction.equalsIgnoreCase("DESC")) {
            sort = Sort.by(Sort.Direction.DESC, sortBy);
        } else {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        }
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    public List<Article> getArticleByTag(String tag, Integer pageNumber, Integer pageSize, String sortBy,
                                         String direction) throws Exception {
        Tag temp = tagService.getTagByName(tag);
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        return articleRepository.findArticleByHashtagsId(temp.getId(), pageable);
    }

    public List<Article> findSuggestArticles(Long sourceArticleId) throws Exception {
        Optional<Article> sourceArticle = articleRepository.findById(sourceArticleId);
        if (sourceArticle.isEmpty()) {
            throw new Exception("article does not exist ...");
        }
        Article source = sourceArticle.get();
        List<Long> sourceTagIds = source.getHashtags().stream().map(Tag::getId).toList();

        List<Long> similarArticlesIds = articleRepository.findSimilarArticleIdByTagId(sourceTagIds);

        similarArticlesIds.remove(source.getId());

        List<Article> similarArticles = new ArrayList<>();
        for (Long id : similarArticlesIds) {
            similarArticles.add(articleRepository.findById(id).get());
        }
        return similarArticles;
    }

    public Article getArticleById(Long id) throws Exception {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            throw new Exception("Article does not exist ...");
        } else {
            return article.get();
        }
    }

    public void deleteAll() {
        articleRepository.deleteAll();
    }

    public List<Article> findArticleBySearchForm(
            String resource,
            String type,
            String keyWord,
            LocalDate startDate,
            LocalDate endDate,
            String tagName,
            String sortBy,
            Integer pageNumber,
            Integer pageSize,
            String direction) throws Exception {
        List<Long> articlesId = articleRepository.findAllArticleId();
        List<Long> searchId = new ArrayList<>();
        List<Article> searchArticle = new ArrayList<>();
        List<HashSet<Long>> search = new ArrayList<>();
        HashSet<Long> temp;

        if (!resource.isEmpty()) {
            Resource resource1 = resourceService.getResourceByName(resource);
            temp = articleRepository.findArticleByResourceId(resource1.getId());
            if (temp.isEmpty()) {
                throw new Exception("No found article with resource is" + resource);
            }
            search.add(temp);
        }

        if (!type.isEmpty()) {
            temp = articleRepository.findByArticleType(type);
            if (temp.isEmpty()) {
                throw new Exception("No found article with type is " + type);
            }
            search.add(temp);
        }

        if (!keyWord.isEmpty()) {
            temp = articleRepository.findByKeyWord(keyWord);
            if (temp.isEmpty()) {
                throw new Exception("No found article contain keyword " + keyWord);
            }
            search.add(temp);
        }

        if (startDate != null && endDate != null) {
            temp = articleRepository.findByCreationDateBetween(startDate, endDate);
            if (temp.isEmpty()) {
                throw new Exception("No found article between " + startDate + " and " + endDate);
            }
            search.add(temp);
        }

        if (!tagName.isEmpty()) {
            Tag tag = tagService.getTagByName(tagName);
            temp = articleRepository.findArticleByHashtagsId(tag.getId());
            if (temp.isEmpty()) {
                throw new Exception("No found article contain tag is " + tagName);
            }
            search.add(temp);
        }

        for (Long id : articlesId) {
            boolean flag = true;
            for (var searchSet : search) {
                if (!searchSet.contains(id)) {
                    flag = false;
                    break;
                }
            }
            if (flag) searchId.add(id);
        }

        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);

        return articleRepository.findAllByIdsIn(searchId, pageable);
    }
}
