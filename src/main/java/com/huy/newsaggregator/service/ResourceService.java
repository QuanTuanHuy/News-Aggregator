package com.huy.newsaggregator.service;

import com.huy.newsaggregator.dto.CreateArticleRequest;
import com.huy.newsaggregator.dto.CreateResourceRequest;
import com.huy.newsaggregator.model.Resource;
import com.huy.newsaggregator.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Resource createResource(CreateResourceRequest createResourceRequest) {
        Resource resource = new Resource();
        resource.setResourceName(createResourceRequest.getResourceName());
        resource.setLink(createResourceRequest.getLink());
        return resourceRepository.save(resource);
    }

    public List<Resource> getAllResource() {
        return resourceRepository.findAll().stream().toList();
    }

    public Resource getResourceById(Long id) throws Exception {
        Optional<Resource> resource =  resourceRepository.findById(id);
        if (resource.isEmpty()) {
            throw new Exception("Resource does not exist");
        }
        return resource.get();
    }

    public Resource getResourceByName(String websiteResource) throws Exception {
        Optional<Resource> resource = resourceRepository.findByResourceName(websiteResource);
        if (resource.isEmpty()) {
            throw new Exception("Resource name does not exist");
        }
        return resource.get();
    }
}
