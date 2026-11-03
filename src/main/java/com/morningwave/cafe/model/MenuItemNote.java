package com.morningwave.cafe.model;

import java.util.Objects;

/** A note belonging to one menu item (one-to-many relation). */
public final class MenuItemNote extends BaseEntity {

    private final long menuItemId;
    private final String content;

    public MenuItemNote(long id, long menuItemId, String name, String content) {
        super(id, name);
        this.menuItemId = menuItemId;
        this.content = Objects.requireNonNull(content, "content");
    }

    public long getMenuItemId() {
        return menuItemId;
    }

    public String getContent() {
        return content;
    }
}
