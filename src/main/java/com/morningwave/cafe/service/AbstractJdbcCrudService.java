package com.morningwave.cafe.service;

import com.morningwave.cafe.db.DBConnection;
import com.morningwave.cafe.db.DataAccessException;
import com.morningwave.cafe.exception.EntityNotFoundException;
import com.morningwave.cafe.model.BaseEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Shared JDBC CRUD algorithm; entity-specific SQL and row/parameter mapping are supplied by subclasses. */
public abstract class AbstractJdbcCrudService<T extends BaseEntity> implements CRUDService<T> {

    @FunctionalInterface
    protected interface StatementBinder {
        void bind(PreparedStatement statement) throws SQLException;
    }

    protected abstract String selectByIdSql();

    protected abstract String selectAllSql();

    protected abstract String selectByNameSql();

    protected abstract String selectSortedByNameSql();

    protected abstract String selectPageSql();

    protected abstract String insertSql();

    protected abstract String updateSql();

    protected abstract String deleteSql();

    protected abstract String batchInsertSql();

    protected abstract T mapRow(ResultSet resultSet) throws SQLException;

    protected abstract void setParameters(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void setUpdateParameters(PreparedStatement statement, T entity) throws SQLException;

    @Override
    public final T findById(long id) throws EntityNotFoundException {
        List<T> result = query(selectByIdSql(), statement -> statement.setLong(1, id));
        if (result.isEmpty()) {
            throw new EntityNotFoundException(id);
        }
        return result.get(0);
    }

    public final List<T> findByName(String name) {
        Objects.requireNonNull(name, "name");
        return query(selectByNameSql(), statement -> statement.setString(1, name));
    }

    @Override
    public final T save(T entity) {
        Objects.requireNonNull(entity, "entity");
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            return save(connection, entity);
        } catch (SQLException e) {
            throw new DataAccessException("Could not save entity with id " + entity.getId(), e);
        }
    }

    protected final T save(Connection connection, T entity) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(updateSql())) {
            setUpdateParameters(update, entity);
            if (update.executeUpdate() > 0) {
                return entity;
            }
        }
        try (PreparedStatement insert = connection.prepareStatement(insertSql())) {
            setParameters(insert, entity);
            insert.executeUpdate();
            return entity;
        }
    }

    @Override
    public final boolean delete(long id) {
        try (Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(deleteSql())) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete entity with id " + id, e);
        }
    }

    @Override
    public final List<T> findAll() {
        return query(selectAllSql(), statement -> { });
    }

    @Override
    public final List<T> findAllSorted(Comparator<? super T> comparator) {
        List<T> result = findAll();
        result.sort(comparator);
        return result;
    }

    @Override
    public final List<T> findAllSortedByName() {
        return query(selectSortedByNameSql(), statement -> { });
    }

    public final List<T> findPage(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("page must be >= 0 and pageSize must be > 0");
        }
        long offset = (long) page * pageSize;
        return query(selectPageSql(), statement -> {
            statement.setInt(1, pageSize);
            statement.setLong(2, offset);
        });
    }

    public final int saveAllBatch(List<T> entities) {
        Objects.requireNonNull(entities, "entities");
        if (entities.isEmpty()) {
            return 0;
        }

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            boolean previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(batchInsertSql())) {
                for (T entity : entities) {
                    setParameters(statement, Objects.requireNonNull(entity, "entity"));
                    statement.addBatch();
                }
                int[] results = statement.executeBatch();
                connection.commit();
                return results.length;
            } catch (SQLException | RuntimeException e) {
                rollback(connection, e);
                throw e;
            } finally {
                connection.setAutoCommit(previousAutoCommit);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not batch-insert entities", e);
        }
    }

    protected final List<T> query(String sql, StatementBinder binder) {
        List<T> result = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            binder.bind(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapRow(resultSet));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Could not execute database query", e);
        }
    }

    protected static void rollback(Connection connection, Throwable failure) {
        try {
            connection.rollback();
        } catch (SQLException rollbackFailure) {
            failure.addSuppressed(rollbackFailure);
        }
    }
}
