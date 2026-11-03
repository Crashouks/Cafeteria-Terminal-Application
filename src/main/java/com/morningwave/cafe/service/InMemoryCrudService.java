package com.morningwave.cafe.service;

import com.morningwave.cafe.exception.EntityNotFoundException;
import com.morningwave.cafe.model.BaseEntity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Універсальне сховище в пам'яті. Один клас працює з будь-якою сутністю, що наслідує BaseEntity.
 * LinkedHashMap&lt;id, T&gt;: пошук/збереження/видалення за id — O(1), порядок додавання зберігається
 * (оновлення наявного ключа порядок не змінює).
 */
public class InMemoryCrudService<T extends BaseEntity> implements CRUDService<T> {

    private final Map<Long, T> storage = new LinkedHashMap<>();

    @Override
    public T findById(long id) throws EntityNotFoundException {
        T entity = storage.get(id);
        if (entity == null) {
            throw new EntityNotFoundException(id);
        }
        return entity;
    }

    @Override
    public T save(T entity) {
        Objects.requireNonNull(entity, "entity");
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public boolean delete(long id) {
        return storage.remove(id) != null;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<T> findAllSorted(Comparator<? super T> comparator) {
        List<T> copy = findAll();
        copy.sort(comparator); // стабільне сортування копії; сховище не чіпаємо
        return copy;
    }

    public int size() {
        return storage.size();
    }
}
