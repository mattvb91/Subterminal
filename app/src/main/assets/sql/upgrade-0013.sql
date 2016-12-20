ALTER TABLE jump ADD COLUMN type INTEGER;
ALTER TABLE jump ADD COLUMN suit_id INTEGER;

CREATE TABLE suits (
    _id INTEGER PRIMARY KEY,
    manufacturer TEXT,
    model TEXT,
    type INTEGER,
    date_in_use DATE,
    serial TEXT,
    synced INTEGER DEFAULT 0,
    deleted INTEGER DEFAULT 0
);