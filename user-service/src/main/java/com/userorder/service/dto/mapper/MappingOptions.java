package com.userorder.service.dto.mapper;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class MappingOptions {

    private Set<String> attributes;
    @Builder.Default
    private boolean withAudit = false;
    
    // Optional reference to the entity class type for more accurate collection detection
    private Class<?> entityClass;

    /**
     * Check if a field is included in the mapping
     * When attributes is null/empty, only include basic fields, not collections
     */
    public boolean includes(String field) {
        if (attributes == null || attributes.isEmpty()) {
            // For basic fields, include them even if attributes is empty
            return !field.contains(".") && !isCollectionField(field);
        }
        return attributes.contains(field);
    }

    /**
     * Check if audit information should be included
     */
    public boolean includeAudit() {
        return withAudit;
    }

    /**
     * Check if a nested attribute path is included
     * Supports nested paths like "roles.permissions"
     */
    public boolean includesPath(String path) {
        if (attributes == null || attributes.isEmpty()) {
            return false; // If no attributes specified, don't include collections or nested paths
        }
        
        // Direct match
        if (attributes.contains(path)) {
            return true;
        }
        
        // Check for nested paths (e.g. "roles.permissions" includes "roles")
        for (String attr : attributes) {
            // Check if the attribute is a sub-path of this path
            if (attr.startsWith(path + ".")) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if a field is a collection, using reflection when possible
     * Falls back to naming patterns if reflection isn't available
     */
    private boolean isCollectionField(String field) {
        // First, try using reflection if entity class is available
        if (entityClass != null) {
            if (MapperUtils.isFieldCollection(entityClass, field)) {
                return true;
            }
        }
        
        // If reflection fails or entity class is not available, 
        // fall back to naming patterns
        
        // Check common collection type suffixes
        if (field.endsWith("List") || 
            field.endsWith("Set") || 
            field.endsWith("Map") || 
            field.endsWith("Collection") ||
            field.endsWith("Array")) {
            return true;
        }
        
        // Check common plural endings
        // Most collections use plural names in Java
        if ((field.endsWith("s") && !field.endsWith("ss") && field.length() > 2) || 
            field.endsWith("es") || 
            (field.endsWith("ies") && field.length() > 4)) {
            return true;
        }
        
        // Check for specific known JPA collection attribute patterns
        // Often collections in JPA entities have "to" in the name for relations
        if (field.contains("To") && 
            (field.startsWith("many") || field.startsWith("one"))) {
            return true;
        }
        
        return false;
    }

    /**
     * Get nested attributes for a specific field
     * For example, if attributes contains "roles.permissions",
     * getNested("roles") would return ["permissions"]
     */
    public Set<String> getNestedAttributes(String field) {
        if (attributes == null || attributes.isEmpty()) {
            return Set.of();
        }
        
        Set<String> nestedAttrs = new HashSet<>();
        String prefix = field + ".";
        
        for (String attr : attributes) {
            // Direct child attributes
            if (attr.startsWith(prefix)) {
                String nestedPart = attr.substring(prefix.length());
                // Only add direct children, not deeply nested paths
                int dotIndex = nestedPart.indexOf('.');
                if (dotIndex > 0) {
                    nestedAttrs.add(nestedPart.substring(0, dotIndex));
                } else {
                    nestedAttrs.add(nestedPart);
                }
            }
        }
        
        return nestedAttrs;
    }
    
    /**
     * Check if this field is part of a nested path in attributes
     * For example, if attributes contains "roles.permissions",
     * isPartOfNestedPath("permissions", "roles") would return true
     */
    public boolean isPartOfNestedPath(String field, String parentField) {
        if (attributes == null || attributes.isEmpty()) {
            return false;
        }
        
        String nestedPath = parentField + "." + field;
        
        // Check if there's a direct match for the nested path
        if (attributes.contains(nestedPath)) {
            return true;
        }
        
        // Check if there's a deeper path that contains this field
        for (String attr : attributes) {
            if (attr.startsWith(nestedPath + ".")) {
                return true;
            }
        }
        
        return false;
    }
}