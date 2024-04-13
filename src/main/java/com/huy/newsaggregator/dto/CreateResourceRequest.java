package com.huy.newsaggregator.dto;

public class CreateResourceRequest {
    private String resourceName;

    private String link;

    public CreateResourceRequest(String resourceName, String link) {
        this.resourceName = resourceName;
        this.link = link;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
