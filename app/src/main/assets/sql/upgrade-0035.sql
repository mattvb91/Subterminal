DELETE FROM exit WHERE global_id IS NOT NULL;
ALTER TABLE exit RENAME TO exit_old;

CREATE TABLE exit (
    _id INTEGER PRIMARY KEY,
    name TEXT,
    rockdrop_distance INTEGER,
    altitude_to_landing INTEGER,
    description TEXT,
    latitude DOUBLE,
    longtitude DOUBLE,
    object_type INTEGER,
    height_unit INTEGER DEFAULT 0,
    synced INTEGER DEFAULT 0,
    deleted INTEGER DEFAULT 0);

INSERT INTO exit SELECT _id, name, rockdrop_distance, altitude_to_landing, description, latitude, longtitude,
object_type, height_unit, synced, deleted FROM exit_old;

DROP TABLE exit_old;
DROP TABLE exit_details;