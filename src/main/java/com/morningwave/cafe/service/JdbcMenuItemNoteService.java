package com.morningwave.cafe.service;

import com.morningwave.cafe.model.MenuItemNote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** JDBC CRUD service for notes associated with menu items. */
public final class JdbcMenuItemNoteService extends AbstractJdbcCrudService<MenuItemNote> {

    private static final String COLUMNS = "id, menu_item_id, name, content";
    private static final String SELECT_COLUMNS = "SELECT " + COLUMNS + " FROM menu_item_notes";

    @Override
    protected String selectByIdSql() {
        return SELECT_COLUMNS + " WHERE id = ?";
    }

    @Override
    protected String selectAllSql() {
        return SELECT_COLUMNS + " ORDER BY id";
    }

    @Override
    protected String selectByNameSql() {
        return SELECT_COLUMNS + " WHERE name = ? ORDER BY id";
    }

    @Override
    protected String selectSortedByNameSql() {
        return SELECT_COLUMNS + " ORDER BY LOWER(name), id";
    }

    @Override
    protected String selectPageSql() {
        return SELECT_COLUMNS + " ORDER BY id LIMIT ? OFFSET ?";
    }

    @Override
    protected String insertSql() {
        return "INSERT INTO menu_item_notes (" + COLUMNS + ") VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String updateSql() {
        return "UPDATE menu_item_notes SET menu_item_id = ?, name = ?, content = ? WHERE id = ?";
    }

    @Override
    protected String deleteSql() {
        return "DELETE FROM menu_item_notes WHERE id = ?";
    }

    @Override
    protected String batchInsertSql() {
        return insertSql();
    }

    @Override
    protected MenuItemNote mapRow(ResultSet resultSet) throws SQLException {
        return new MenuItemNote(resultSet.getLong("id"), resultSet.getLong("menu_item_id"),
                resultSet.getString("name"), resultSet.getString("content"));
    }

    @Override
    protected void setParameters(PreparedStatement statement, MenuItemNote entity) throws SQLException {
        statement.setLong(1, entity.getId());
        setNoteParameters(statement, entity, 2);
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, MenuItemNote entity) throws SQLException {
        setNoteParameters(statement, entity, 1);
        statement.setLong(4, entity.getId());
    }

    void insert(Connection connection, MenuItemNote entity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(insertSql())) {
            setParameters(statement, entity);
            statement.executeUpdate();
        }
    }

    private static void setNoteParameters(PreparedStatement statement, MenuItemNote entity, int firstIndex)
            throws SQLException {
        statement.setLong(firstIndex, entity.getMenuItemId());
        statement.setString(firstIndex + 1, entity.getName());
        statement.setString(firstIndex + 2, entity.getContent());
    }
}
