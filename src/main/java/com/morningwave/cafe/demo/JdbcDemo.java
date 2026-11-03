package com.morningwave.cafe.demo;

import com.morningwave.cafe.db.DataAccessException;
import com.morningwave.cafe.exception.EntityNotFoundException;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.model.Drink;
import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.model.MenuItemNote;
import com.morningwave.cafe.service.JdbcMenuItemNoteService;
import com.morningwave.cafe.service.JdbcMenuItemService;
import java.util.ArrayList;
import java.util.List;

/** Reproducible demonstrations for the JDBC lab; run with --jdbc-demo. */
public final class JdbcDemo {

    private static final long DEMO_ITEM_ID = 7_000_001L;
    private static final long ROLLBACK_ITEM_ID = 7_000_002L;
    private static final long DEMO_NOTE_ID = 7_000_001L;
    private static final int BATCH_SIZE = 500;

    private JdbcDemo() {
    }

    public static void run() throws MenuException {
        JdbcMenuItemService items = new JdbcMenuItemService();
        JdbcMenuItemNoteService notes = new JdbcMenuItemNoteService();

        demonstrateCrudAndSafeSearch(items, notes);
        demonstrateRollback(items, notes);
        benchmarkBatchInsert(items);
    }

    private static void demonstrateCrudAndSafeSearch(
            JdbcMenuItemService items, JdbcMenuItemNoteService notes) throws MenuException {
        items.delete(DEMO_ITEM_ID);
        items.delete(ROLLBACK_ITEM_ID);
        notes.delete(DEMO_NOTE_ID);

        Drink drink = new Drink(DEMO_ITEM_ID, "JDBC кава", 55, 5, 250);
        items.save(drink);
        items.save(new Drink(DEMO_ITEM_ID, "JDBC кава оновлена", 60, 5, 250));
        System.out.println("CRUD findById: " + items.findById(DEMO_ITEM_ID));
        System.out.println("SQL ORDER BY (назва, ціна): " + items.findAllSortedByNameAndByPrice().size()
                + " запис(ів)");
        System.out.println("Сторінка 0 (до 5 записів): " + items.findPage(0, 5).size() + " запис(ів)");

        notes.save(new MenuItemNote(DEMO_NOTE_ID, DEMO_ITEM_ID, "Демо", "Нотатка до позиції меню"));
        String injectionAttempt = "' OR '1'='1";
        System.out.println("PreparedStatement з ін'єкційним рядком повернув "
                + items.findByName(injectionAttempt).size() + " запис(ів).");
    }

    private static void demonstrateRollback(JdbcMenuItemService items, JdbcMenuItemNoteService notes)
            throws MenuException {
        try {
            items.saveWithNote(
                    new Drink(ROLLBACK_ITEM_ID, "Має бути скасовано", 25, 1, 200),
                    new MenuItemNote(DEMO_NOTE_ID, ROLLBACK_ITEM_ID, "Дублікат", "Навмисний конфлікт PK"));
            throw new IllegalStateException("Expected a duplicate note id to fail");
        } catch (DataAccessException expected) {
            try {
                items.findById(ROLLBACK_ITEM_ID);
                throw new IllegalStateException("Transaction rollback did not remove the menu item");
            } catch (EntityNotFoundException rolledBack) {
                System.out.println("Транзакцію скасовано після конфлікту PK; позиції "
                        + ROLLBACK_ITEM_ID + " у БД немає.");
            }
        }
    }

    private static void benchmarkBatchInsert(JdbcMenuItemService items) throws MenuException {
        List<MenuItem> batch = new ArrayList<>(BATCH_SIZE);
        long firstId = 7_100_000L;
        for (int i = 0; i < BATCH_SIZE; i++) {
            long id = firstId + i;
            items.delete(id);
            batch.add(new Drink(id, "Batch drink " + i, 20 + i % 10, i % 100, 250));
        }

        long singleStart = System.nanoTime();
        for (MenuItem item : batch) {
            items.save(item);
        }
        long singleMillis = (System.nanoTime() - singleStart) / 1_000_000;

        for (MenuItem item : batch) {
            items.delete(item.getId());
        }
        long batchStart = System.nanoTime();
        int inserted = items.saveAllBatch(batch);
        long batchMillis = (System.nanoTime() - batchStart) / 1_000_000;
        System.out.println("Вимірювання " + BATCH_SIZE + " записів: по одному — " + singleMillis
                + " мс; addBatch/executeBatch — " + batchMillis + " мс (" + inserted + " пакетних записів).");

        for (MenuItem item : batch) {
            items.delete(item.getId());
        }
    }
}
