package com.morningwave.cafe.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/** Singleton EntityManagerFactory with one short-lived EntityManager per operation. */
public final class JpaUtil {

    private final EntityManagerFactory entityManagerFactory;

    private JpaUtil() {
        Properties config = loadDatabaseProperties();
        Map<String, Object> overrides = Map.of(
                "jakarta.persistence.jdbc.driver", "org.h2.Driver",
                "jakarta.persistence.jdbc.url", System.getProperty("app.db.url", config.getProperty("db.url")),
                "jakarta.persistence.jdbc.user", System.getProperty("app.db.username", config.getProperty("db.username")),
                "jakarta.persistence.jdbc.password", System.getProperty("app.db.password", config.getProperty("db.password")));
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("cafeteria", overrides);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Could not initialize Hibernate persistence unit 'cafeteria'", e);
        }
    }

    private static class Holder {
        private static final JpaUtil INSTANCE = new JpaUtil();
    }

    public static JpaUtil getInstance() {
        return Holder.INSTANCE;
    }

    public <T> T inTransaction(Function<EntityManager, T> operation) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            T result = operation.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public <T> T read(Function<EntityManager, T> operation) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return operation.apply(entityManager);
        } finally {
            entityManager.close();
        }
    }

    public void close() {
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    private static Properties loadDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream stream = JpaUtil.class.getResourceAsStream("/application.properties")) {
            if (stream == null) {
                throw new IllegalStateException("Database configuration not found: /application.properties");
            }
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not read database configuration", e);
        }
    }
}
