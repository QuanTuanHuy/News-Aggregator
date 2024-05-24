package com.huy.newsaggregator.exception;

import java.util.Arrays;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object... fieldValue) {
        super(resourceName + " not found with " + fieldName + " : '" + Arrays.toString(fieldValue) + "'");
    }
}
