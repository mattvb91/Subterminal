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

INSERT INTO exit (
    _id,
    name ,
    rockdrop_distance ,
    altitude_to_landing ,
    difficulty_tracking_exit ,
    difficulty_tracking_freefall ,
    difficulty_tracking_landing ,
    difficulty_tracking_overall ,
    difficulty_wingsuit_exit ,
    difficulty_wingsuit_freefall ,
    difficulty_wingsuit_landing ,
    difficulty_wingsuit_overall ,
    description ,
    rules ,
    lat ,
    long,
    object_type) VALUES (
        NULL,
        "Nose 1",
        390,
        455,
        2,
        2,
        2,
        2,
        2,
        3,
        2,
        3,
        "Description of jump <br/>
        <br/>
        More description of jump bla blabl
        blablabla",
        "SPECIFIC RULES FOR THIS JUMP_<br/>
        <br/>
         Air Glacier airspace!<br/>
         Call Air Glacier before every jump: +41 33 856 05 60<br/>
         Check for helicopters visually and acoustically",
         46.579998,
         7.905217,
         4
    );

CREATE TABLE image (
        _id INTEGER PRIMARY KEY,
        filename TEXT,
        entity_type INTEGER,
        entity_id INTEGER,
        synced INTEGER DEFAULT 0
);