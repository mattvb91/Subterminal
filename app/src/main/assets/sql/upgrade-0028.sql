CREATE TABLE skydive_tunnel (
        _id INTEGER PRIMARY KEY,
        name TEXT,
        description TEXT,
        latitude DOUBLE,
        longtitude DOUBLE,
        website TEXT,
        phone TEXT,
        email TEXT,
        formatted_address TEXT,
        local TEXT,
        country TEXT,
        tunnel_diameter,
        tunnel_height,
        featured INTEGER DEFAULT 0
);

CREATE TABLE skydive_tunnel_session (
        _id INTEGER PRIMARY KEY,
        date DATE,
        description TEXT,
        length INTEGER,
        tunnel_id INTEGER,
        synced INTEGER DEFAULT 0,
        deleted INTEGER DEFAULT 0
);