/*
* B.A.S.E Tables
*/
CREATE TABLE gear (
    _id INTEGER PRIMARY KEY,
    container_manufacturer TEXT,
    container_type TEXT,
    container_serial TEXT,
    container_date_in_use DATE,
    canopy_manufacturer TEXT,
    canopy_type TEXT,
    canopy_serial TEXT,
    canopy_date_in_use DATE,
    synced INTEGER DEFAULT 0,
    deleted INTEGER DEFAULT 0);

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
    height_unit INTEGER DEFAULT 0,
    synced INTEGER DEFAULT 0,
    deleted INTEGER DEFAULT 0);

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
    difficulty_wingsuit_overall INTEGER);

CREATE TABLE jump (
        _id INTEGER PRIMARY KEY,
        date DATE,
        exit_id INTEGER,
        gear_id INTEGER,
        pc_size INTEGER,
        slider INTEGER,
        delay INTEGER,
        description TEXT,
        type INTEGER,
        suit_id INTEGER,
        synced INTEGER DEFAULT 0,
        deleted INTEGER DEFAULT 0);


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


CREATE TABLE image (
        _id INTEGER PRIMARY KEY,
        filename TEXT,
        entity_type INTEGER,
        entity_id INTEGER,
        synced INTEGER DEFAULT 0,
        deleted INTEGER DEFAULT 0
);


CREATE TABLE signature (
        _id INTEGER PRIMARY KEY,
        entity_type INTEGER,
        entity_id INTEGER,
        print_name TEXT,
        synced INTEGER DEFAULT 0,
        deleted INTEGER DEFAULT 0
);

/*
 * Skydiving Tables
 */
CREATE TABLE skydive (
        _id INTEGER PRIMARY KEY,
        dropzone_id TEXT,
        date DATE,
        rig_id INTEGER,
        aircraft_id INTEGER,
        exit_altitude INTEGER,
        deploy_altitude INTEGER,
        delay INTEGER,
        description TEXT,
        jump_type INTEGER,
        suit_id INTEGER,
        cutaway INTEGER DEFAULT 0,
        height_unit INTEGER DEFAULT 1,
        synced INTEGER DEFAULT 0,
        deleted INTEGER DEFAULT 0
);

CREATE TABLE skydive_dropzone (
        _id INTEGER PRIMARY KEY,
        name TEXT,
        global_id INTEGER,
        description TEXT,
        latitude DOUBLE,
        longtitude DOUBLE,
        website TEXT,
        phone TEXT,
        email TEXT,
        formatted_address TEXT,
        local TEXT,
        country TEXT
);

CREATE TABLE skydive_rig (
    _id INTEGER PRIMARY KEY,
    container_manufacturer TEXT,
    container_model TEXT,
    container_serial TEXT,
    container_date_in_use DATE,
    main_manufacturer TEXT,
    main_model TEXT,
    main_serial TEXT,
    main_date_in_use DATE,
    reserve_manufacturer TEXT,
    reserve_model TEXT,
    reserve_serial TEXT,
    reserve_date_in_use DATE,
    aad_manufacturer TEXT,
    aad_model TEXT,
    aad_serial TEXT,
    aad_date_in_use DATE,
    synced INTEGER DEFAULT 0,
    deleted INTEGER DEFAULT 0);

CREATE TABLE skydive_aircraft (
    _id INTEGER PRIMARY KEY,
    name TEXT
);

CREATE TABLE skydive_dropzone_aircraft (
    _id INTEGER PRIMARY KEY,
    dropzone_id INTEGER,
    aircraft_id INTEGER
);