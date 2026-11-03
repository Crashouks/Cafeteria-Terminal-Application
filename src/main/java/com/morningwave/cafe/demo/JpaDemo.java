package com.morningwave.cafe.demo;

import com.morningwave.cafe.exception.EntityNotFoundException;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.model.Drink;
import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.model.MenuItemNote;
import com.morningwave.cafe.service.JpaMenuItemService;
import org.hibernate.LazyInitializationException;

/** Reproducible Hibernate CRUD, JPQL, relationship, and fetching demonstrations. */
public final class JpaDemo {

    private JpaDemo() {
    }

    public static void run() throws MenuException {
        JpaMenuItemService items = new JpaMenuItemService();
        Drink created = new Drink(0, "Hibernate demo", 39, 4, 250);
        MenuItemNote note = new MenuItemNote(0, created, "Hibernate note", "One-to-many demo note");
        items.saveWithNote(created, note);
        long id = created.getId();
        System.out.println("CREATE: згенерований id=" + id);

        Drink second = new Drink(0, "Hibernate demo second", 41, 5, 300);
        items.saveWithNote(second, new MenuItemNote(0, second, "Second note", "N+1 example"));

        MenuItem detached = items.findById(id);
        System.out.println("READ через JPQL: " + detached);
        try {
            detached.getNotes().size();
            throw new IllegalStateException("Expected lazy access on a detached entity to fail");
        } catch (LazyInitializationException expected) {
            System.out.println("LazyInitializationException відтворено: колекція недоступна після закриття EntityManager.");
        }

        MenuItem loadedWithNotes = items.findByIdWithNotes(id);
        System.out.println("Виправлення через JOIN FETCH: нотаток="
                + loadedWithNotes.getNotes().size() + " після закриття EntityManager.");
        System.out.println("JPQL з іменованим параметром знайшов: "
                + items.findByName("Hibernate demo").size() + " запис(ів).");

        System.out.println("N+1: нотаток=" + items.countNotesWithNPlusOne()
                + "; виправлений fetch join: нотаток=" + items.countNotesWithFetchJoin()
                + ". SQL у консолі показує кількість SELECT.");

        Drink updated = new Drink(id, "Hibernate demo updated", 42, 4, 250);
        items.save(updated);
        System.out.println("UPDATE: " + items.findById(id));

        if (!items.delete(id)) {
            throw new IllegalStateException("Could not delete the Hibernate demo entity");
        }
        if (!items.delete(second.getId())) {
            throw new IllegalStateException("Could not delete the second Hibernate demo entity");
        }
        try {
            items.findById(id);
            throw new IllegalStateException("Deleted Hibernate demo entity is still present");
        } catch (EntityNotFoundException expected) {
            System.out.println("DELETE: запис видалений.");
        }
    }
}
