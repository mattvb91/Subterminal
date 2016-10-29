ALTER TABLE exit ADD COLUMN global_id TEXT;

CREATE TABLE exit_details (
    _id INTEGER PRIMARY KEY,
    exit_id INTEGER,
    rules TEXT,
    difficulty_tracking_exit INTEGER,
    difficulty_tracking_freefall INTEGER,
    difficulty_tracking_landing INTEGER,
    difficulty_tracking_overall INTEGER,
    difficulty_wingsuit_exit INTEGER,
    difficulty_wingsuit_freefall INTEGER,
    difficulty_wingsuit_landing INTEGER,
    difficulty_wingsuit_overall INTEGER
);

BEGIN TRANSACTION;
CREATE TEMPORARY TABLE t1_backup(
    _id INTEGER PRIMARY KEY,
    global_id TEXT,
    name TEXT,
    rockdrop_distance INTEGER,
    altitude_to_landing INTEGER,
    description TEXT,
    lat TEXT,
    long TEXT,
    object_type INTEGER
);

INSERT INTO t1_backup SELECT _id,global_id,name,rockdrop_distance,altitude_to_landing,description,lat,long,object_type FROM exit;
DROP TABLE exit;

CREATE TABLE exit (
    _id INTEGER PRIMARY KEY,
    global_id TEXT,
    name TEXT,
    rockdrop_distance INTEGER,
    altitude_to_landing INTEGER,
    description TEXT,
    lat TEXT,
    long TEXT,
    object_type INTEGER);

INSERT INTO exit SELECT _id,global_id,name,rockdrop_distance,altitude_to_landing,description,lat,long,object_type FROM t1_backup;
DROP TABLE t1_backup;
COMMIT;