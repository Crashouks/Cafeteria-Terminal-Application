package com.morningwave.cafe.service;

import com.morningwave.cafe.exception.EntityNotFoundException;
import com.morningwave.cafe.model.BaseEntity;
import java.util.Comparator;
import java.util.List;

/**
 * Типізований CRUD-контракт.
 * Обмеження {@code T extends BaseEntity} дає змогу всередині сервісу викликати getId()/getName():
 * без нього T був би просто Object — не було б ключа для сховища й назви для сортування.
 */
public interface CRUDService<T extends BaseEntity> {

    /** @throws EntityNotFoundException якщо запису з таким id немає */
    T findById(long id) throws EntityNotFoundException;

    /** Додає нову сутність або замінює наявну з тим самим id. Повертає збережену сутність. */
    T save(T entity);

    /** @return true, якщо запис існував і був видалений; false, якщо такого id не було */
    boolean delete(long id);

    /** Копія всіх записів у порядку зберігання. */
    List<T> findAll();

    /**
     * Нова відсортована колекція; сховище не змінюється.
     * {@code Comparator<? super T>}: компаратор для предка (напр. Comparator&lt;BaseEntity&gt;)
     * вміє порівнювати і нащадків, тож його можна передати сервісу для T = Drink.
     * З {@code Comparator<T>} довелося б писати окремий компаратор для кожного підтипу.
     */
    List<T> findAllSorted(Comparator<? super T> comparator);

    /** Сортування за назвою; реалізовано через findAllSorted, тому однакове для всіх сутностей. */
    default List<T> findAllSortedByName() {
        return findAllSorted(BaseEntity.BY_NAME);
    }
}
