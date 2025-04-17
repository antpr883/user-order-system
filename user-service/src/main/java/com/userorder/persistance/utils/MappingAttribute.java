package com.userorder.persistance.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark entity attributes that should be available for dynamic entity graph loading.
 * This allows for runtime decisions about which associations to fetch eagerly.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappingAttribute {

    /**
     * The path to the attribute, defaults to the field name.
     */
    String path() default "";

    /**
     * Whether to allow loading sub-attributes of this attribute.
     * When true, paths like "attribute.subAttribute" will be allowed.
     */
    boolean withSubAttributes() default false;

    /**
     * The target entity class for collection fields.
     */
    Class<?> targetEntity() default Void.class;

    /**
     * Description of the attribute for documentation purposes.
     */
    String description() default "";
}