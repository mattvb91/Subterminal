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