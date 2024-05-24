package com.huy.newsaggregator.service.Imp;

import com.huy.newsaggregator.dto.CreateResourceRequest;
import com.huy.newsaggregator.exception.ResourceNotFoundException;
import com.huy.newsaggregator.model.Resource;
import com.huy.newsaggregator.repository.ResourceRepository;
import com.huy.newsaggregator.service.IResourceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService implements IResourceService {
    private final ResourceRepository resourceRepository;

    ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public Resource createResource(CreateResourceRequest createResourceRequest) {
        String name = createResourceRequest.getResourceName();
        Optional<Resource> checkIfExist = resourceRepository.findByResourceName(name);
        if (checkIfExist.isPresent()) return null;

        Resource resource = new Resource();
        resource.setResourceName(createResourceRequest.getResourceName());
        resource.setLink(createResourceRequest.getLink());
        return resourceRepository.save(resource);
    }

    @Override
    public List<Resource> getAllResource() {
        return resourceRepository.findAll().stream().toList();
    }

    @Override
    public Resource getResourceById(Long id) throws Exception {
        Optional<Resource> resource =  resourceRepository.findById(id);
        if (resource.isEmpty()) {
            throw new Exception("Resource does not exist");
        }
        return resource.get();
    }

    @Override
    public Resource getResourceByName(String websiteResource) throws ResourceNotFoundException {
        Optional<Resource> resource = resourceRepository.findByResourceName(websiteResource);
        if (resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource", "name", websiteResource);
        }
        return resource.get();
    }
}
