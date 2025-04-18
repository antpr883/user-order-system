package com.userorder.service;

import com.userorder.service.dto.base.DTO;
import com.userorder.service.dto.mappers.MappingOptions;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Базовий інтерфейс сервісу з підтримкою CRUD операцій і динамічного завантаження атрибутів.
 *
 * @param <E> Тип сутності
 * @param <D> Тип DTO
 */
public interface BaseService<E, D extends DTO> {

    /**
     * Знаходить сутність за ID з конфігурованим набором атрибутів
     */
    D findById(Long id, boolean includeAudit, Set<String> attributes);

    /**
     * Знаходить сутність за ID з базовими атрибутами
     */
    default D findById(Long id) {
        return findById(id, false, null);
    }

    /**
     * Повертає всі сутності з конфігурованим набором атрибутів
     */
    List<D> findAll(boolean includeAudit, Set<String> attributes);

    /**
     * Повертає всі сутності з базовими атрибутами
     */
    default List<D> findAll() {
        return findAll(false, null);
    }

    /**
     * Зберігає нову сутність
     */
    D save(D dto);

    /**
     * Оновлює існуючу сутність
     */
    D update(D dto, MappingOptions options);

    /**
     * Оновлює частково існуючу сутність (тільки ненульові поля)
     */
    default D update(D dto) {
        MappingOptions options = MappingOptions.builder()
                .collectionStrategy(MappingOptions.CollectionHandlingStrategy.MERGE)
                .build();
        return update(dto, options);
    }

    /**
     * Видаляє сутність за ID
     */
    void deleteById(Long id);

    /**
     * Перевіряє чи існує сутність із зазначеним ID
     */
    boolean existsById(Long id);

    /**
     * Знаходить сутність (не DTO) за ID
     */
    Optional<E> findEntityById(Long id);
}