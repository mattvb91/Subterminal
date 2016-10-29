CREATE TABLE gear (
    _id INTEGER PRIMARY KEY,
    container_manufacturer TEXT,
    container_type TEXT,
    container_serial TEXT,
    container_date_in_use DATE,
    canopy_manufacturer TEXT,
    canopy_type TEXT,
    canopy_serial TEXT,
    canopy_date_in_use DATE );

CREATE TABLE exit (
    _id INTEGER PRIMARY KEY,
    global_id TEXT,
    name TEXT,
    rockdrop_distance INTEGER,
    altitude_to_landing INTEGER,
    difficulty_tracking_exit INTEGER,
    difficulty_tracking_freefall INTEGER,
    difficulty_tracking_landing INTEGER,
    difficulty_tracking_overall INTEGER,
    difficulty_wingsuit_exit INTEGER,
    difficulty_wingsuit_freefall INTEGER,
    difficulty_wingsuit_landing INTEGER,
    difficulty_wingsuit_overall INTEGER,
    description TEXT,
    rules TEXT,
    lat TEXT,
    long TEXT,
    object_type INTEGER);

CREATE TABLE jump (
        _id INTEGER PRIMARY KEY,
        date DATE,
        exit_id INTEGER,
        gear_id INTEGER,
        pc_size INTEGER,
        slider INTEGER,
        delay INTEGER,
        description TEXT);

CREATE TABLE image (
        _id INTEGER PRIMARY KEY,
        filename TEXT,
        entity_type INTEGER,
        entity_id INTEGER,
        synced INTEGER DEFAULT 0
);