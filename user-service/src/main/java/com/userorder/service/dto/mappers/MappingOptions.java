package com.userorder.service.dto.mappers;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

/**
 * Configuration class for controlling dynamic attribute loading in mappers
 */
@Data
@Builder
public class MappingOptions {
    private Set<String> attributes;

    @Builder.Default
    private boolean includeAudit = false;

    /**
     * Check if a specific attribute should be included
     *
     * @param attribute The attribute name to check
     * @return true if the attribute should be included
     */
    public boolean includes(String attribute) {
        if (attribute == null) {
            return false;
        }

        if (attributes == null || attributes.isEmpty()) {
            return !attribute.contains("."); // Basic fields only when no attributes specified
        }
        return attributes.contains(attribute);
    }

    /**
     * Check if a path or any sub-path should be included
     *
     * @param path The path to check
     * @return true if the path or any sub-path should be included
     */
    public boolean includesPath(String path) {
        if (path == null) {
            return false;
        }

        if (attributes == null || attributes.isEmpty()) {
            return false;
        }

        // Direct match
        if (attributes.contains(path)) {
            return true;
        }

        // Check for nested paths
        for (String attr : attributes) {
            if (attr.startsWith(path + ".")) {
                return true;
            }
        }

        return false;
    }
}