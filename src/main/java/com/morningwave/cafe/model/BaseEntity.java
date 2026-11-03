package com.morningwave.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import org.hibernate.proxy.HibernateProxy;
import java.util.Comparator;
import java.util.Objects;

/**
 * Базовий клас усіх сутностей, якими керує CRUD-сервіс (ЛР7–9).
 * Ідентичність сутності визначає лише {@code id}, який не змінюється після створення.
 */
@MappedSuperclass
public abstract class BaseEntity {

    /** Порівняння за назвою без урахування регістру (спільний для всіх сутностей). */
    public static final Comparator<BaseEntity> BY_NAME =
            Comparator.comparing(BaseEntity::getName, String.CASE_INSENSITIVE_ORDER);

    @Id
    @GeneratedValue(generator = "entity_seq")
    @SequenceGenerator(name = "entity_seq", sequenceName = "entity_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    protected BaseEntity() {
    }

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
        if (o == null || persistentClass(this) != persistentClass(o)) {
            return false;
        }
        return id == ((BaseEntity) o).id;
    }

    private static Class<?> persistentClass(Object entity) {
        if (entity instanceof HibernateProxy proxy) {
            return proxy.getHibernateLazyInitializer().getPersistentClass();
        }
        return entity.getClass();
    }

    @Override
    public final int hashCode() {
        return Long.hashCode(id);
    }
}
