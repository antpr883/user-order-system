package com.userorder.persistance.utils;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

@Component
public class GraphBuilderMappingService {

    private static final Logger log = LoggerFactory.getLogger(GraphBuilderMappingService.class);

    /**
     * Creates an EntityGraph with the specified attributes for the given root class.
     *
     * @param rootClass The root entity class
     * @param attributes Collection of attribute paths to include in the graph
     * @return An EntityGraph configured with the specified attributes
     */
    public EntityGraph getGraphWithAttributes(Class<?> rootClass, Collection<String> attributes) {
        DynamicEntityGraph.Builder builder = DynamicEntityGraph.fetching();

        for (String attributePath : attributes) {
            addPathIfValid(builder, rootClass, attributePath);
        }

        return builder.build();
    }

    private void addPathIfValid(DynamicEntityGraph.Builder builder, Class<?> clazz, String attributePath) {
        String[] parts = attributePath.split("\\.");
        Class<?> currentClass = clazz;
        StringBuilder pathBuilder = new StringBuilder();

        for (String fieldName : parts) {
            try {
                Field field = currentClass.getDeclaredField(fieldName);
                MappingAttribute mapping = field.getAnnotation(MappingAttribute.class);
                if (mapping == null) {
                    throw new IllegalArgumentException("Field '" + fieldName + "' in class " + currentClass.getSimpleName() + " is not annotated with @MappingAttribute");
                }

                if (!pathBuilder.isEmpty()) {
                    pathBuilder.append(".");
                }
                pathBuilder.append(fieldName);

                builder.addPath(pathBuilder.toString());

                if (Collection.class.isAssignableFrom(field.getType())) {
                    ParameterizedType type = (ParameterizedType) field.getGenericType();
                    currentClass = (Class<?>) type.getActualTypeArguments()[0];
                } else {
                    currentClass = field.getType();
                }

            } catch (NoSuchFieldException e) {
                throw new RuntimeException("No such field: " + fieldName + " in " + currentClass.getName(), e);
            }
        }
    }
}
