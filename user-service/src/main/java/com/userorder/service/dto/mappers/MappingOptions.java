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
     * Стратегія обробки пов'язаних колекцій
     */
    @Builder.Default
    private CollectionHandlingStrategy collectionStrategy = CollectionHandlingStrategy.MERGE;

    /**
     * Перевірка, чи слід включати певний атрибут
     *
     * @param attribute Назва атрибуту для перевірки
     * @return true, якщо атрибут має бути включений
     */
    public boolean includes(String attribute) {
        if (attribute == null) {
            return false;
        }

        if (attributes == null || attributes.isEmpty()) {
            return !attribute.contains("."); // Тільки прості поля, якщо атрибути не вказані
        }
        return attributes.contains(attribute);
    }

    /**
     * Перевірка, чи слід включити шлях або будь-який підшлях
     *
     * @param path Шлях для перевірки
     * @return true, якщо шлях або будь-який підшлях має бути включений
     */
    public boolean includesPath(String path) {
        if (path == null) {
            return false;
        }

        if (attributes == null || attributes.isEmpty()) {
            return false;
        }

        // Пряме співпадіння
        if (attributes.contains(path)) {
            return true;
        }

        // Перевірка вкладених шляхів
        for (String attr : attributes) {
            if (attr.startsWith(path + ".")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Стратегія обробки пов'язаних колекцій
     */
    public enum CollectionHandlingStrategy {
        /**
         * Зберегти існуючі елементи і додати або оновити елементи з DTO
         */
        MERGE,

        /**
         * Замінити всі існуючі елементи елементами з DTO
         */
        REPLACE,

        /**
         * Не змінювати пов'язані колекції
         */
        IGNORE
    }
}