package dev.danvega.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ném khi không tìm thấy resource (Article, Page, ...).
 * Ánh xạ tự động sang HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("%s không tìm thấy với %s = '%s'".formatted(resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName    = fieldName;
        this.fieldValue   = fieldValue;
    }

    public String getResourceName() { return resourceName; }
    public String getFieldName()    { return fieldName; }
    public Object getFieldValue()   { return fieldValue; }
}
