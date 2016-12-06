ALTER TABLE exit RENAME TO exit_old;

CREATE TABLE exit (
    _id INTEGER PRIMARY KEY,
    global_id TEXT,
    name TEXT,
    rockdrop_distance INTEGER,
    altitude_to_landing INTEGER,
    description TEXT,
    latitude DOUBLE,
    longtitude DOUBLE,
    object_type INTEGER,
    synced INTEGER DEFAULT 0,
    deleted INTEGER DEFAULT 0);

INSERT INTO exit SELECT *  FROM exit_old;

DROP TABLE exit_old;

ALTER TABLE image ADD COLUMN deleted INTEGER DEFAULT 0;