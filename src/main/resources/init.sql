CREATE TABLE IF NOT EXISTS menu_items (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    kind VARCHAR(20) NOT NULL CHECK (kind IN ('DRINK', 'HOT_DRINK', 'DESSERT')),
    price DOUBLE PRECISION NOT NULL CHECK (price > 0),
    calories INTEGER NOT NULL CHECK (calories >= 0),
    volume_ml INTEGER,
    temperature_c INTEGER,
    weight_g INTEGER
);

CREATE TABLE IF NOT EXISTS menu_item_notes (
    id BIGINT PRIMARY KEY,
    menu_item_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    CONSTRAINT fk_menu_item_notes_item
        FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_menu_item_notes_menu_item_id ON menu_item_notes(menu_item_id);

CREATE SEQUENCE IF NOT EXISTS entity_seq START WITH 10000000 INCREMENT BY 1;
