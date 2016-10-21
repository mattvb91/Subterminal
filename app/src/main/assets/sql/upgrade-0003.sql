CREATE TABLE image (
    _id INTEGER PRIMARY KEY,
    filename TEXT,
    entity_type INTEGER,
    entity_id INTEGER,
    synced INTEGER DEFAULT 0
);