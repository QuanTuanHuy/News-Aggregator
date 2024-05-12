package com.huy.newsaggregator.service;

import com.huy.newsaggregator.dto.CreateResourceRequest;
import com.huy.newsaggregator.model.Resource;

import java.util.List;

public interface IResourceService {
    Resource createResource(CreateResourceRequest createResourceRequest);
    List<Resource> getAllResource();
    Resource getResourceById(Long id) throws Exception;
    Resource getResourceByName(String websiteResource) throws Exception;
}
