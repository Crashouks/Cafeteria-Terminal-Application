package com.morningwave.cafe.service;

import com.morningwave.cafe.db.JpaUtil;
import com.morningwave.cafe.exception.EntityNotFoundException;
import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.model.MenuItemNote;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

/** Jakarta Persistence implementation of the existing menu-item service contract. */
public final class JpaMenuItemService implements MenuItemService {

    private final JpaUtil jpa = JpaUtil.getInstance();

    @Override
    public MenuItem findById(long id) throws EntityNotFoundException {
        MenuItem item = jpa.read(entityManager -> entityManager.find(MenuItem.class, id));
        if (item == null) {
            throw new EntityNotFoundException(id);
        }
        return item;
    }

    /** Fetches the lazy notes with one JPQL query, so they remain accessible after this method returns. */
    public MenuItem findByIdWithNotes(long id) throws EntityNotFoundException {
        List<MenuItem> results = jpa.read(entityManager -> entityManager.createQuery(
                        "select distinct item from MenuItem item left join fetch item.notes where item.id = :id",
                        MenuItem.class)
                .setParameter("id", id)
                .getResultList());
        if (results.isEmpty()) {
            throw new EntityNotFoundException(id);
        }
        return results.get(0);
    }

    @Override
    public MenuItem save(MenuItem entity) {
        Objects.requireNonNull(entity, "entity");
        return jpa.inTransaction(entityManager -> save(entityManager, entity));
    }

    public MenuItem saveWithNote(MenuItem item, MenuItemNote note) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(note, "note");
        item.addNote(note);
        return jpa.inTransaction(entityManager -> save(entityManager, item));
    }

    private static MenuItem save(EntityManager entityManager, MenuItem item) {
        if (item.getId() != 0 && entityManager.find(MenuItem.class, item.getId()) != null) {
            return entityManager.merge(item);
        }
        entityManager.persist(item);
        return item;
    }

    @Override
    public boolean delete(long id) {
        return jpa.inTransaction(entityManager -> {
            MenuItem item = entityManager.find(MenuItem.class, id);
            if (item == null) {
                return false;
            }
            entityManager.remove(item);
            return true;
        });
    }

    @Override
    public List<MenuItem> findAll() {
        return jpa.read(entityManager -> entityManager.createQuery(
                        "select item from MenuItem item order by item.id", MenuItem.class)
                .getResultList());
    }

    @Override
    public List<MenuItem> findAllSorted(java.util.Comparator<? super MenuItem> comparator) {
        List<MenuItem> items = findAll();
        items.sort(comparator);
        return items;
    }

    @Override
    public List<MenuItem> findAllSortedByName() {
        return jpa.read(entityManager -> entityManager.createQuery(
                        "select item from MenuItem item order by lower(item.name), item.id", MenuItem.class)
                .getResultList());
    }

    @Override
    public List<MenuItem> findAllSortedByNameAndByPrice() {
        return jpa.read(entityManager -> entityManager.createQuery(
                        "select item from MenuItem item order by lower(item.name), item.price, item.id",
                        MenuItem.class)
                .getResultList());
    }

    public List<MenuItem> findByName(String name) {
        return jpa.read(entityManager -> entityManager.createQuery(
                        "select item from MenuItem item where item.name = :name order by item.id", MenuItem.class)
                .setParameter("name", name)
                .getResultList());
    }

    public List<MenuItem> findPage(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("page must be >= 0 and pageSize must be > 0");
        }
        return jpa.read(entityManager -> entityManager.createQuery(
                        "select item from MenuItem item order by item.id", MenuItem.class)
                .setFirstResult(Math.toIntExact((long) page * pageSize))
                .setMaxResults(pageSize)
                .getResultList());
    }

    /** Reads all items and initializes each lazy collection inside one persistence context (N+1 example). */
    public int countNotesWithNPlusOne() {
        return jpa.read(entityManager -> {
            List<MenuItem> items = entityManager.createQuery("select item from MenuItem item", MenuItem.class)
                    .getResultList();
            int count = 0;
            for (MenuItem item : items) {
                count += item.getNotes().size();
            }
            return count;
        });
    }

    /** Replaces the N+1 selects with a single fetch-join query. */
    public int countNotesWithFetchJoin() {
        return jpa.read(entityManager -> {
            List<MenuItem> items = entityManager.createQuery(
                            "select distinct item from MenuItem item left join fetch item.notes",
                            MenuItem.class)
                    .getResultList();
            int count = 0;
            for (MenuItem item : items) {
                count += item.getNotes().size();
            }
            return count;
        });
    }
}
