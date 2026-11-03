package com.morningwave.cafe.model;

import java.util.Comparator;
import java.util.Objects;

/**
 * Базовий клас усіх сутностей, якими керує CRUD-сервіс (ЛР7–9).
 * Ідентичність сутності визначає лише {@code id}, який не змінюється після створення.
 */
public abstract class BaseEntity {

    /** Порівняння за назвою без урахування регістру (спільний для всіх сутностей). */
    public static final Comparator<BaseEntity> BY_NAME =
            Comparator.comparing(BaseEntity::getName, String.CASE_INSENSITIVE_ORDER);

    private final long id;
    private final String name;

    protected BaseEntity(long id, String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name");
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * equals/hashCode — лише за id: це «первинний ключ». Два об'єкти з одним id — той самий запис,
     * навіть якщо ціна чи назва різні (наприклад, стара й нова версія після оновлення).
     * getClass() замість instanceof зберігає симетрію між різними типами сутностей.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return id == ((BaseEntity) o).id;
    }

    @Override
    public final int hashCode() {
        return Long.hashCode(id);
    }
}
