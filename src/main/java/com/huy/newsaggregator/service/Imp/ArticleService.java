package com.huy.newsaggregator.service.Imp;

import com.huy.newsaggregator.dto.CreateArticleRequest;
import com.huy.newsaggregator.exception.ResourceNotFoundException;
import com.huy.newsaggregator.model.Article;
import com.huy.newsaggregator.model.Resource;
import com.huy.newsaggregator.model.Tag;
import com.huy.newsaggregator.repository.ArticleRepository;

import java.time.LocalDate;
import java.util.*;

import com.huy.newsaggregator.repository.TagRepository;
import com.huy.newsaggregator.service.IArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ArticleService implements IArticleService {
    private final ArticleRepository articleRepository;

    private final TagService tagService;

    private final ResourceService resourceService;

    public ArticleService(ArticleRepository articleRepository, TagService tagService, ResourceService resourceService) {
        this.articleRepository = articleRepository;
        this.tagService = tagService;
        this.resourceService = resourceService;
    }

    @Override
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

        try {
            return articleRepository.save(article);
        } catch (Exception e) {
            throw new Exception("Something went wrong");
        }
    }

    @Override
    public List<Article> createListArticle(List<CreateArticleRequest> reqs) throws Exception {
        List<Article> articles = new ArrayList<>();
        for (var req : reqs) {
            articles.add(createArticle(req));
        }
        return articles;
    }

    @Override
    public List<Article> getAllArticle() {
        return articleRepository.findAll();
    }

    @Override
    public Map<String, Object> getArticleByResource(
        String resource, Integer pageNumber, Integer pageSize, String sortBy, String direction) throws Exception {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        Resource resource1 = resourceService.getResourceByName(resource);

        Page<Article> pageArticles = articleRepository.findArticleByResourceId(resource1.getId(), pageable);

        return toPageArticleResponse(pageArticles);
    }

    @Override
    public List<Article> getArticleByKeyWord(
            String keyWord, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        String searchKey = convertKeyWordSearch(keyWord);
        return articleRepository.findByKeyWordWithRank(searchKey, pageable);
    }


    @Override
    public List<Article> getArticleByType(
            String type, Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);
        return articleRepository.findByArticleType(type, pageable);
    }

    @Override
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

    @Override
    public Map<String, Object> getArticleByTag(String tag, Integer pageNumber, Integer pageSize, String sortBy,
                                               String direction) throws Exception {
        Tag temp = tagService.getTagByName(tag);
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, direction);

        Page<Article> pageArticles = articleRepository.findArticleByHashtagsId(temp.getId(), pageable);

        return toPageArticleResponse(pageArticles);
    }

    @Override
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

    @Override
    public Article getArticleById(Long id) throws Exception {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            throw new Exception("Article does not exist ...");
        } else {
            return article.get();
        }
    }


    @Override
    public Map<String, Object> findArticleBySearchForm(
            String resource,
            String type,
            String keyWord,
            LocalDate startDate,
            LocalDate endDate,
            String tagName,
            Integer pageNumber,
            Integer pageSize) throws ResourceNotFoundException {
        List<Long> articlesId = articleRepository.findAllArticleId();
        List<Long> searchId = new ArrayList<>();
        List<HashSet<Long>> search = new ArrayList<>();
        HashSet<Long> temp;

        if (!resource.isEmpty()) {
            Resource resource1 = resourceService.getResourceByName(resource);
            temp = articleRepository.findArticleByResourceId(resource1.getId());
            if (temp.isEmpty()) {
                throw new ResourceNotFoundException("Article", "resource", resource);
            }
            search.add(temp);
        }

        if (!type.isEmpty()) {
            temp = articleRepository.findByArticleType(type);
            if (temp.isEmpty()) {
                throw new ResourceNotFoundException("Article", "type", type);
            }
            search.add(temp);
        }

        if (startDate != null && endDate != null) {
            temp = articleRepository.findByCreationDateBetween(startDate, endDate);
            if (temp.isEmpty()) {
                throw new ResourceNotFoundException("Article", "creationDate", startDate, endDate);
            }
            search.add(temp);
        }

        if (!tagName.isEmpty()) {
            Tag tag = tagService.getTagByName(tagName);
            temp = articleRepository.findArticleByHashtagsId(tag.getId());
            if (temp.isEmpty()) {
                throw new ResourceNotFoundException("Article", "tag", tagName);
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

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> pageArticles = null;

        if (!keyWord.isEmpty()) {
            String searchKey = convertKeyWordSearch(keyWord);
            pageArticles = articleRepository.findByKeyWordWithRankAndInListId(searchKey, searchId, pageable);
        } else {
            pageArticles = articleRepository.findAllByIdsIn(searchId, pageable);
        }

        return toPageArticleResponse(pageArticles);
    }

    public Map<String, Object> toPageArticleResponse(Page<Article> pageArticles) {
        Map<String, Object> articlesResponse = new HashMap<>();
        articlesResponse.put("articles", pageArticles.getContent());
        articlesResponse.put("currentPage", pageArticles.getNumber());
        articlesResponse.put("totalItems", pageArticles.getTotalElements());
        articlesResponse.put("totalPages", pageArticles.getTotalPages());

        return articlesResponse;
    }

    @Override
    public void deleteAll() {
        articleRepository.deleteAll();
    }

    private String convertKeyWordSearch(String keyWord) {
        String[] arrKeys = keyWord.split(",");
        StringBuilder keyword = new StringBuilder();
        for (String s : arrKeys) {
            keyword.append("+");
            keyword.append(s);
            keyword.append(" ");
        }
        return keyword.toString();
    }
}
