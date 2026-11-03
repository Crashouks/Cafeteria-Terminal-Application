package com.morningwave.cafe.service;

import com.morningwave.cafe.db.DBConnection;
import com.morningwave.cafe.db.DataAccessException;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.model.Dessert;
import com.morningwave.cafe.model.Drink;
import com.morningwave.cafe.model.HotDrink;
import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.model.MenuItemNote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/** JDBC-backed menu service with SQL ordering, batch insertion, and a multi-table transaction. */
public final class JdbcMenuItemService extends AbstractJdbcCrudService<MenuItem> implements MenuItemService {

    private static final String COLUMNS =
            "id, name, kind, price, calories, volume_ml, temperature_c, weight_g";
    private static final String SELECT_COLUMNS = "SELECT " + COLUMNS + " FROM menu_items";
    private final JdbcMenuItemNoteService noteService = new JdbcMenuItemNoteService();

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
        return "INSERT INTO menu_items (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String updateSql() {
        return "UPDATE menu_items SET name = ?, kind = ?, price = ?, calories = ?, "
                + "volume_ml = ?, temperature_c = ?, weight_g = ? WHERE id = ?";
    }

    @Override
    protected String deleteSql() {
        return "DELETE FROM menu_items WHERE id = ?";
    }

    @Override
    protected String batchInsertSql() {
        return insertSql();
    }

    @Override
    protected MenuItem mapRow(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        int calories = resultSet.getInt("calories");
        String kind = resultSet.getString("kind");
        try {
            return switch (kind) {
                case "DRINK" -> new Drink(id, name, price, calories, resultSet.getInt("volume_ml"));
                case "HOT_DRINK" -> new HotDrink(id, name, price, calories, resultSet.getInt("volume_ml"),
                        resultSet.getInt("temperature_c"));
                case "DESSERT" -> new Dessert(id, name, price, calories, resultSet.getInt("weight_g"));
                default -> throw new SQLException("Unsupported menu item kind: " + kind);
            };
        } catch (MenuException e) {
            throw new SQLException("Invalid menu item stored in database (id=" + id + ")", e);
        }
    }

    @Override
    protected void setParameters(PreparedStatement statement, MenuItem entity) throws SQLException {
        statement.setLong(1, entity.getId());
        setItemParameters(statement, entity, 2);
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, MenuItem entity) throws SQLException {
        setItemParameters(statement, entity, 1);
        statement.setLong(8, entity.getId());
    }

    private static void setItemParameters(PreparedStatement statement, MenuItem entity, int firstIndex)
            throws SQLException {
        statement.setString(firstIndex, entity.getName());
        statement.setString(firstIndex + 1, kindOf(entity));
        statement.setDouble(firstIndex + 2, entity.getPrice());
        statement.setInt(firstIndex + 3, entity.getCalories());
        if (entity instanceof Drink drink) {
            statement.setInt(firstIndex + 4, drink.getVolumeMl());
            if (entity instanceof HotDrink hotDrink) {
                statement.setInt(firstIndex + 5, hotDrink.getTemperatureC());
            } else {
                statement.setNull(firstIndex + 5, java.sql.Types.INTEGER);
            }
            statement.setNull(firstIndex + 6, java.sql.Types.INTEGER);
        } else if (entity instanceof Dessert dessert) {
            statement.setNull(firstIndex + 4, java.sql.Types.INTEGER);
            statement.setNull(firstIndex + 5, java.sql.Types.INTEGER);
            statement.setInt(firstIndex + 6, dessert.getWeightG());
        } else {
            throw new SQLException("Unsupported menu item type: " + entity.getClass().getName());
        }
    }

    private static String kindOf(MenuItem entity) throws SQLException {
        if (entity instanceof HotDrink) {
            return "HOT_DRINK";
        }
        if (entity instanceof Drink) {
            return "DRINK";
        }
        if (entity instanceof Dessert) {
            return "DESSERT";
        }
        throw new SQLException("Unsupported menu item type: " + entity.getClass().getName());
    }

    /** Saves a menu item and its note atomically. */
    public MenuItem saveWithNote(MenuItem item, MenuItemNote note) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(note, "note");
        if (item.getId() != note.getMenuItemId()) {
            throw new IllegalArgumentException("The note must refer to the menu item being saved");
        }

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            boolean previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                save(connection, item);
                noteService.insert(connection, note);
                connection.commit();
                return item;
            } catch (SQLException | RuntimeException e) {
                rollback(connection, e);
                throw e;
            } finally {
                connection.setAutoCommit(previousAutoCommit);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not save menu item and note in one transaction", e);
        }
    }

    @Override
    public List<MenuItem> findAllSortedByNameAndByPrice() {
        return query(SELECT_COLUMNS + " ORDER BY LOWER(name), price, id", statement -> { });
    }
}
