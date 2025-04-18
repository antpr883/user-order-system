package com.userorder.service.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ParamUtils {
    /**
     * Helper method to parse comma-separated attributes into a Set
     */
    public static Set<String> parseAttributesParam(String attributes) {
        if (attributes == null || attributes.trim().isEmpty()) {
            return null;
        }
        return new HashSet<>(Arrays.asList(attributes.split(",")));
    }
}
