package com.morningwave.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

/** A note belonging to one menu item (one-to-many relation). */
@Entity
@Table(name = "menu_item_notes")
public final class MenuItemNote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @jakarta.persistence.Transient
    private long legacyMenuItemId;

    protected MenuItemNote() {
        super();
    }

    public MenuItemNote(long id, long menuItemId, String name, String content) {
        super(id, name);
        this.legacyMenuItemId = menuItemId;
        this.content = Objects.requireNonNull(content, "content");
    }

    public long getMenuItemId() {
        return menuItem == null ? legacyMenuItemId : menuItem.getId();
    }

    public String getContent() {
        return content;
    }

    public MenuItemNote(long id, MenuItem menuItem, String name, String content) {
        super(id, name);
        this.content = Objects.requireNonNull(content, "content");
        setMenuItem(Objects.requireNonNull(menuItem, "menuItem"));
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        this.legacyMenuItemId = menuItem == null ? 0 : menuItem.getId();
    }
}
