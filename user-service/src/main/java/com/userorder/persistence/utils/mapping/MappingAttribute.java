package com.userorder.persistence.utils.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappingAttribute {

    String path() default "";

    boolean withSubAttributes() default false;

    Class<?> targetEntity() default Void.class;

    String description() default "";
}
