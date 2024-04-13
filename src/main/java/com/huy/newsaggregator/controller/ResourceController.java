package com.huy.newsaggregator.controller;

import com.huy.newsaggregator.dto.CreateResourceRequest;
import com.huy.newsaggregator.model.Resource;
import com.huy.newsaggregator.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/resources")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<Resource> createResource(@RequestBody CreateResourceRequest createResourceRequest) {
        Resource resource = resourceService.createResource(createResourceRequest);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Resource>> getAllResource() {
        List<Resource> resources = resourceService.getAllResource();
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable Long id) throws Exception {
        Resource resource = resourceService.getResourceById(id);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
