package com.userorder.service.dto.mapper;


import com.userorder.persistence.model.Address;
import com.userorder.persistence.model.Contact;
import com.userorder.persistence.model.User;
import com.userorder.persistence.model.base.PersistenceModel;
import org.mapstruct.Named;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility methods for mappers to avoid circular references and handle
 * common mapping patterns consistently
 */
public class MapperUtils {

    /**
     * Extract IDs from a collection of entities
     */
    @Named("extractIds")
    public static <T extends PersistenceModel> Set<Long> extractIds(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptySet();
        }
        return entities.stream()
                .filter(Objects::nonNull)
                .map(PersistenceModel::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Check if an entity has a specific role in its ancestry
     * to prevent infinite recursion during mapping
     */
    @Named("hasAncestorOfType")
    public static boolean hasAncestorOfType(PersistenceModel entity, Class<?> type) {
        if (entity == null) {
            return false;
        }

        // Check based on entity type
        if (entity instanceof Address address) {
            return type == User.class && address.getUser() != null;
        } else if (entity instanceof Contact contact) {
            return type == User.class && contact.getUser() != null;
        }
        
        return false;
    }
    
    /**
     * Safe count of collection size
     */
    @Named("safeCount")
    public static <T> int safeCount(Collection<T> collection) {
        return collection != null ? collection.size() : 0;
    }

    /**
     * Maps null to empty set to avoid null pointer exceptions
     */
    @Named("emptyIfNull")
    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    /**
     * Checks if a collection is not empty
     */
    @Named("isNotEmpty")
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }


    /**
     * Checks if a field in a class is of collection type
     * This is a more robust approach than string-based heuristics
     */
    @Named("isFieldCollection")
    public static boolean isFieldCollection(Class<?> clazz, String fieldName) {
        try {
            Field field = getFieldRecursively(clazz, fieldName);
            if (field == null) {
                return false;
            }
            
            return Collection.class.isAssignableFrom(field.getType()) ||
                   Map.class.isAssignableFrom(field.getType()) ||
                   field.getType().isArray();
        } catch (Exception e) {
            // If we can't determine, fall back to naming convention
            return false;
        }
    }
    
    /**
     * Gets a field from a class or its superclasses
     */
    private static Field getFieldRecursively(Class<?> clazz, String fieldName) {
        if (clazz == null || clazz == Object.class) {
            return null;
        }
        
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // If not found, try the superclass
            return getFieldRecursively(clazz.getSuperclass(), fieldName);
        }
    }
}