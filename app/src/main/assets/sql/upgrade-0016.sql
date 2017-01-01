
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