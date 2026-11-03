package com.morningwave.cafe.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/** Singleton access to a connection pool; callers receive and close a pooled connection per operation. */
public final class DBConnection {

    private static final String CONFIG_RESOURCE = "/application.properties";
    private static final String INIT_RESOURCE = "/init.sql";

    private final HikariDataSource dataSource;

    private DBConnection() {
        Properties properties = loadProperties();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getProperty("app.db.url", properties.getProperty("db.url")));
        config.setUsername(System.getProperty("app.db.username", properties.getProperty("db.username")));
        config.setPassword(System.getProperty("app.db.password", properties.getProperty("db.password")));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.pool.maximum-size", "10")));
        config.setPoolName("cafeteria-db-pool");
        dataSource = new HikariDataSource(config);
    }

    private static class Holder {
        private static final DBConnection INSTANCE = new DBConnection();
    }

    public static DBConnection getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        dataSource.close();
    }

    public synchronized void initializeDatabase() {
        String script;
        try (InputStream stream = DBConnection.class.getResourceAsStream(INIT_RESOURCE)) {
            if (stream == null) {
                throw new IllegalStateException("Database initialization script not found: " + INIT_RESOURCE);
            }
            script = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read database initialization script", e);
        }

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            for (String sql : script.split(";")) {
                if (!sql.isBlank()) {
                    statement.execute(sql);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not initialize the database", e);
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream stream = DBConnection.class.getResourceAsStream(CONFIG_RESOURCE)) {
            if (stream == null) {
                throw new IllegalStateException("Database configuration not found: " + CONFIG_RESOURCE);
            }
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not read database configuration", e);
        }
    }
}
