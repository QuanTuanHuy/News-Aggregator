package com.huy.newsaggregator.repository;

import com.huy.newsaggregator.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByResourceName(String websiteResource);
}
